package pl.edu.agh.po.controller;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.util.Pair;
import javafx.embed.swing.SwingFXUtils;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.input.Clipboard;
import org.apache.commons.codec.binary.Base32;
import pl.edu.agh.po.exceptions.UserAlreadyExistsException;
import pl.edu.agh.po.model.*;
import pl.edu.agh.po.service.AuthenticationService;
import pl.edu.agh.po.service.DeviceService;
import pl.edu.agh.po.service.ReportService;
import pl.edu.agh.po.service.UserService;
import pl.edu.agh.po.utilities.PasswordEncryption;


public class Controller{

    @FXML private VBox loginPanel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label loginMessageLabel;
    @FXML private VBox adminPanel;
    @FXML private Label welcomeAdminLabel;
    @FXML private Label adminRoleLabel;
    @FXML private VBox ceoPanel;
    @FXML private Label welcomeCeoLabel;
    @FXML private Label ceoRoleLabel;
    @FXML private VBox technicianPanel;
    @FXML private Label welcomeTechnicianLabel;
    @FXML private Label technicianRoleLabel;
    @FXML private Button loginButton;

    private final DeviceService deviceService = new DeviceService();
    private final AuthenticationService authService = AuthenticationService.getInstance();
    private final UserService userService = new UserService();
    private final ReportService reportService = ReportService.getInstance();
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    @FXML
    public void initialize() {
        setupLogging();
        showLoginPanel();
    }

    private void setupLogging() {
        try {
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.info("logger initialized");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    private void showPanelForRole(User user)
    {
        switch (user.getRole()) {
            case ADMIN -> showAdminPanel();
            case CEO -> showCeoPanel();
            case TECHNICIAN -> showTechnicianPanel();
            default -> throw new IllegalStateException(
                    "Unknown role: " + user.getRole());
        }
    }
    private void promptForTotpCode(User user) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Two-Factor Authentication");
        dialog.setHeaderText("Enter TOTP code from your authenticator app");
        dialog.setContentText("Code:");

        dialog.showAndWait().ifPresent(codeInput -> {
            try {
                // Pobranie sekretu z bazy
                String secret = user.getTotpSecret();
                if (secret == null || secret.isBlank()) {
                    throw new IllegalStateException("TOTP secret is empty!");
                }

                byte[] keyBytes = new Base32().decode(secret);
                SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");

                // Generator TOTP
                TimeBasedOneTimePasswordGenerator totp =
                        new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30));

                // Weryfikacja ±1 krokiem (±30 sekund)
                boolean valid = false;
                Instant now = Instant.now();
                for (int i = -1; i <= 1; i++) {
                    Instant t = now.plusSeconds(i * 30);
                    String expected = String.format("%06d", totp.generateOneTimePassword(key, t));
                    if (expected.equals(codeInput)) {
                        valid = true;
                        break;
                    }
                }

                if (valid) {
                    // Poprawny kod: zapisujemy stan użytkownika
                    user.setForceTotpSetup(false);
                    user.setTotpEnabled(true);
                    userService.update(user);

                    // Logowanie i pokazanie panelu
                    completeLogin(user);
                    showPanelForRole(user);

                } else {
                    // Błędny kod: retry
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Invalid code");
                    error.setHeaderText("The TOTP code is incorrect. Please try again.");
                    error.showAndWait();

                    promptForTotpCode(user); // wywołanie rekursywne
                }

            } catch (Exception e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("TOTP verification failed!");
                error.showAndWait();
            }
        });
    }
    private void handleFirstLoginTotp(User user) {
        try {
            // Sprawdzenie, czy użytkownik musi skonfigurować TOTP
            if (!user.isForceTotpSetup()) {
                return; // nic nie robimy, jeśli nie wymuszone
            }

            if (user.getTotpSecret() == null || user.getTotpSecret().isBlank()) {
                KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA1");
                keyGen.init(160);
                SecretKey secretKey = keyGen.generateKey();

                String base32Secret = new Base32().encodeToString(secretKey.getEncoded());

                user.setTotpSecret(base32Secret);
                user.setTotpEnabled(false);
                userService.update(user);

                String otpAuthUrl =
                        "otpauth://totp/YourApp:" + user.getUsername() +
                                "?secret=" + base32Secret +
                                "&issuer=YourApp&digits=6";

                BitMatrix matrix = new MultiFormatWriter()
                        .encode(otpAuthUrl, BarcodeFormat.QR_CODE, 200, 200);

                Image fxImage = SwingFXUtils.toFXImage(
                        MatrixToImageWriter.toBufferedImage(matrix), null);

                ImageView qrView = new ImageView(fxImage);

                Alert qrAlert = new Alert(Alert.AlertType.INFORMATION);
                qrAlert.setTitle("Set up 2FA");
                qrAlert.setHeaderText("Scan this QR code with your TOTP authentication app");
                qrAlert.getDialogPane().setContent(qrView);
                qrAlert.showAndWait();
            }
            // 🔁 2. TYLKO pytamy o kod (może być retry)
            promptForTotpCode(user);

        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
        }
    }

    private void completeLogin(User user) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Success");
        info.setHeaderText("Logging in");
        info.setContentText("Verification successful!");
        info.showAndWait();

        showPanelForRole(user);
    }

    private void showTotpPrompt(User user) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Two-Factor Authentication");
        dialog.setHeaderText("Enter TOTP code from your authenticator app");
        dialog.setContentText("Code:");

        dialog.showAndWait().ifPresent(codeInput -> {
            try {
                String secret = user.getTotpSecret();
                if (secret == null || secret.isBlank()) {
                    throw new IllegalStateException("TOTP secret is empty!");
                }
                byte[] keyBytes = new Base32().decode(secret);
                SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");

                // Tworzymy generator TOTP
                TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30));

                // Generujemy aktualny kod TOTP
                String expectedCode = String.format("%06d",
                        totp.generateOneTimePassword(key, java.time.Instant.now()));

                if (expectedCode.equals(codeInput)) {
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Success");
                    info.setHeaderText("TOTP verified! Logging in...");
                    info.showAndWait();

                    // Po poprawnym kodzie pokazujemy panel wg roli
                    showPanelForRole(user);
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Invalid TOTP code!");
                    error.showAndWait();

                    // Pozwalamy użytkownikowi spróbować ponownie
                    showTotpPrompt(user);
                }
            } catch (Exception e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("TOTP verification failed!");
                error.showAndWait();
            }
        });
    }

    private void showPasswordChangeDialog(User user, boolean afterBootstrap) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter new password:");

        PasswordField newPasswordField = new PasswordField();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("New Password:"), 0, 0);
        grid.add(newPasswordField, 1, 0);
        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return newPasswordField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newPassword -> {
            if (!newPassword.isBlank()) {
                user.setPassword(PasswordEncryption.hash(newPassword));
                user.setForcePasswordChange(false);
                try {
                    userService.update(user);
                } catch (RuntimeException e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Failed to update password");
                    error.setContentText(e.getMessage());
                    error.showAndWait();
                    return;
                }

                if (afterBootstrap || user.isForceTotpSetup()) {
                    // po zmianie hasła konfigurujemy TOTP
                    handleFirstLoginTotp(user);
                } else {
                    completeLogin(user);
                    showPanelForRole(user);
                }
            }
        });
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        usernameField.clear();
        passwordField.clear();

        loginButton.setDisable(true);
        loginMessageLabel.setText("Logging in...");

        new Thread(() -> {
            AuthenticationService.AuthStatus status =
                    authService.login(username, password);
            User user = authService.getCurrentUser();

            Platform.runLater(() -> {
                // GUI MUSI być na FX thread

                loginButton.setDisable(false);

                switch (status) {
                    case SUCCESS -> {
                        loginMessageLabel.setText("");
                        logger.info("user: " + username + " (" + user.getRole() + ") logged in");
                        showPanelForRole(user);
                    }

                    case BOOTSTRAP_REQUIRED -> {
                        loginMessageLabel.setText("Bootstrap admin: set password & enable 2FA");
                        showBootstrapSetup();
                    }

                    case PASSWORD_CHANGE_REQUIRED -> {
                        loginMessageLabel.setText("Password change required");
                        showPasswordChangeDialog(user, false);
                    }

                    case TOTP_REQUIRED -> {
                        loginMessageLabel.setText("");
                        if (user.isForceTotpSetup()) {
                            handleFirstLoginTotp(user);
                        } else {
                            showTotpPrompt(user);
                        }
                    }

                    case INVALID_CREDENTIALS -> {
                        loginMessageLabel.setText("Invalid credentials!");
                    }
                }
            });
        }).start();
    }

    @FXML
    private void handleLogout() {
        logger.info("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") logged out");
        authService.logout();
        showLoginPanel();
    }
    private void showBootstrapSetup() {
        User user = authService.getCurrentUser();

        // 1. Dialog zmiany hasła
        TextInputDialog passwordDialog = new TextInputDialog();
        passwordDialog.setTitle("Bootstrap Admin Setup");
        passwordDialog.setHeaderText("Set a new password for bootstrap admin");
        passwordDialog.setContentText("New password:");

        passwordDialog.showAndWait().ifPresent(newPassword -> {
            if (!newPassword.isBlank()) {
                try {
                    // Zapisz nowe hasło
                    user.setPassword(newPassword);
                    user.setForcePasswordChange(false);
                    user.setBootstrap(false);

                    // Generowanie sekretu TOTP
                    KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA1");
                    keyGen.init(160); // standard TOTP = 160 bitów
                    SecretKey secretKey = keyGen.generateKey();

                    // zapis Base32 do bazy
                    Base32 base32 = new Base32();
                    String base32Secret = base32.encodeToString(secretKey.getEncoded()).replace("=", ""); // usuń padding
                    user.setTotpSecret(base32Secret);
                    user.setTotpEnabled(true);
                    userService.update(user);

                    // Generowanie URL do Google Authenticator
                    // Format: otpauth://totp/<issuer>:<account>?secret=<secret>&issuer=<issuer>&digits=6
                    String otpAuthUrl = "otpauth://totp/YourApp:" + user.getUsername() +
                            "?secret=" + base32Secret +
                            "&issuer=YourApp&digits=6";

                    // Tworzymy QR code
                    BitMatrix matrix = new MultiFormatWriter().encode(otpAuthUrl, BarcodeFormat.QR_CODE, 200, 200);
                    BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);
                    Image fxImage = SwingFXUtils.toFXImage(qrImage, null);

                    // Pokazanie QR code w dialogu
                    ImageView qrView = new ImageView(fxImage);
                    qrView.setFitWidth(200);
                    qrView.setFitHeight(200);

                    Alert qrAlert = new Alert(Alert.AlertType.INFORMATION);
                    qrAlert.setTitle("Set up 2FA");
                    qrAlert.setHeaderText("Scan this QR code with your TOTP authentication app");
                    qrAlert.getDialogPane().setContent(qrView);
                    qrAlert.showAndWait();

                    // Po skonfigurowaniu TOTP od razu prompt do kodu
                    showTotpPrompt(user);

                } catch (Exception e) {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Error");
                    error.setHeaderText("Failed to setup bootstrap");
                    error.showAndWait();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Password cannot be empty!");
                alert.showAndWait();
            }
        });
    }

    @FXML
    private void handleExit() {
        logger.info("app exited");
        Platform.exit();
    }
    private void hideAllPanels() {
        loginPanel.setVisible(false);
        adminPanel.setVisible(false);
        ceoPanel.setVisible(false);
        technicianPanel.setVisible(false);
    }
    private void showLoginPanel() {
        hideAllPanels();
        loginPanel.setVisible(true);
    }
    private void showAdminPanel() {
        hideAllPanels();
        adminPanel.setVisible(true);
        welcomeAdminLabel.setText("logged in as: " + authService.getCurrentUser().getUsername());
        adminRoleLabel.setText("user role: admin");
    }
    private void showCeoPanel() {
        hideAllPanels();
        ceoPanel.setVisible(true);
        welcomeCeoLabel.setText("logged in as: " + authService.getCurrentUser().getUsername());
        ceoRoleLabel.setText("user role: ceo");
    }
    private void showTechnicianPanel() {
        hideAllPanels();
        technicianPanel.setVisible(true);
        welcomeTechnicianLabel.setText("logged in as: " + authService.getCurrentUser().getUsername());
        technicianRoleLabel.setText("user role: technician");
    }
    @FXML
    private void handleAddUser() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Dodaj użytkownika");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        ChoiceBox<UserRole> roleChoiceBox = new ChoiceBox<>();
        roleChoiceBox.getItems().addAll(UserRole.ADMIN, UserRole.CEO, UserRole.TECHNICIAN);
        roleChoiceBox.setValue(UserRole.TECHNICIAN);

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Role:"), 0, 2);
        grid.add(roleChoiceBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(usernameField.getText(), passwordField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(credentials -> {

            User newUser = new User(
                    credentials.getKey(),
                    credentials.getValue(),
                    roleChoiceBox.getValue()
            );

            newUser.setForceTotpSetup(true);
            newUser.setForcePasswordChange(true);

            new Thread(() -> {
                try {
                    userService.createUser(newUser);

                    Platform.runLater(() -> {
                        logger.info("User added: " + newUser.getUsername());
                    });

                } catch (UserAlreadyExistsException e) {

                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Nie można dodać użytkownika");
                        alert.setContentText(
                                "Użytkownik o takiej nazwie już istnieje.\nWybierz inną nazwę."
                        );
                        alert.showAndWait();
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    @FXML
    private void handleBlockUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete user");
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            try {
                userService.deleteUserByUsername(username);
                logger.info("User: " + authService.getCurrentUser().getUsername()
                        + " (" + authService.getCurrentUser().getRole() + ") deleted user: " + username);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("User deleted");
                alert.setContentText("User " + username + " was successfully deleted.");
                alert.showAndWait();

            } catch (RuntimeException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to delete user");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }

    @FXML
    private void handleShowDatabase() {
        StringBuilder usersDB = new StringBuilder();
        for (User user : userService.getAllUsers()) {
            usersDB.append("ID: ").append(user.getId()).append("\n");
            usersDB.append("Username: ").append(user.getUsername()).append("\n");
            usersDB.append("Role: ").append(user.getRole()).append("\n");
            usersDB.append("-------------------\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Baza użytkowników");
        alert.setHeaderText("Lista użytkowników");
        alert.setContentText(usersDB.toString());
        alert.showAndWait();
        logger.info("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") looked at database");
    }

    @FXML
    private void handleCreateReport() {
        String report = reportService.generateFullRaport();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String filename = "raport_" + timestamp + ".md";
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(report);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Raport");
            alert.setHeaderText("Raport został wygenerowany");
            alert.setContentText("Zapisano do pliku: " + filename);
            alert.showAndWait();
            logger.info("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") created new report");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void handleAddDevice() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Dodaj urządzenie");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ChoiceBox<DeviceType> typeChoice = new ChoiceBox<>();
        typeChoice.getItems().addAll(DeviceType.ROUTER, DeviceType.SWITCH, DeviceType.ACCESS_POINT);
        typeChoice.setValue(DeviceType.ROUTER);

        ChoiceBox<DeviceStatus> statusChoice = new ChoiceBox<>();
        statusChoice.getItems().addAll(DeviceStatus.AVAILABLE, DeviceStatus.BROKEN, DeviceStatus.MAINTANCE, DeviceStatus.IN_USE);
        statusChoice.setValue(DeviceStatus.AVAILABLE);

        TextField modelField = new TextField();
        TextField hostnameField = new TextField();
        TextField ethField = new TextField();
        TextField configField = new TextField();

        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeChoice, 1, 0);
        grid.add(new Label("Status:"), 0, 1);
        grid.add(statusChoice, 1, 1);
        grid.add(new Label("Model:"), 0, 2);
        grid.add(modelField, 1, 2);
        grid.add(new Label("Hostname:"), 0, 3);
        grid.add(hostnameField, 1, 3);
        grid.add(new Label("Ethernet Interfaces:"), 0, 4);
        grid.add(ethField, 1, 4);
        grid.add(new Label("Configuration:"), 0, 5);
        grid.add(configField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return "OK";
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(r -> {
            try {
                Device device = new Device(
                    typeChoice.getValue(),
                    statusChoice.getValue(),
                    modelField.getText(),
                    hostnameField.getText(),
                    Integer.parseInt(ethField.getText()),
                    configField.getText()
                );
                new Thread(() -> deviceService.addDevice(device)).start();
                logger.info("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") added new device to database with ID: " + device.getId());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid number format");
                alert.showAndWait();
                logger.warning("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") tried to add new device to database but insert invalid number format");
            }
        });
    }
    @FXML
    private void handleChangeConfiguration() {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Zmień konfigurację");
        idDialog.setContentText("Device ID:");

        Optional<String> idResult = idDialog.showAndWait();
        idResult.ifPresent(idStr -> {
            try {
                long id = Long.parseLong(idStr);
                Device device = deviceService.getDeviceById(id);
                if (device != null) {
                    Dialog<String> configDialog = new Dialog<>();
                    configDialog.setTitle("Zmień konfigurację");

                    ButtonType pasteButton = new ButtonType("Wklej ze schowka", ButtonBar.ButtonData.OK_DONE);
                    configDialog.getDialogPane().getButtonTypes().addAll(pasteButton, ButtonType.CANCEL);

                    Label label = new Label("Kliknij 'Wklej ze schowka' aby wkleić konfigurację");
                    configDialog.getDialogPane().setContent(label);

                    configDialog.setResultConverter(dialogButton -> {
                        if (dialogButton == pasteButton) {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            return clipboard.getString();
                        }
                        return null;
                    });

                    Optional<String> configResult = configDialog.showAndWait();
                    configResult.ifPresent(config -> {
                        deviceService.updateConfiguration(id,config);
                        logger.info("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") change configuration of device with ID: " + device.getId());
                    });
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid ID");
                alert.showAndWait();
                logger.warning("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") tried to change configuration of device but provided invalid ID");
            }
        });
    }
    @FXML
    private void handleDeleteDevice() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Usuń urządzenie");
        dialog.setContentText("Device ID:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(idStr -> {
            try {
                long id = Long.parseLong(idStr);
                new Thread(() -> deviceService.deleteDevice(id)).start();
                logger.info("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") deleted device with ID: " + id);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid ID");
                alert.showAndWait();
                logger.warning("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") tried to delet device but provided invalid ID");
            }
        });
    }

    @FXML
    private void handleShowDevices() {
        StringBuilder devices = new StringBuilder();
        devices.append("BAZA URZĄDZEŃ\n");
        devices.append("===================\n\n");

        for (Device device : deviceService.getAllDevices()) {
            devices.append("ID: ").append(device.getId()).append("\n");
            devices.append("Type: ").append(device.getType()).append("\n");
            devices.append("Status: ").append(device.getStatus()).append("\n");
            devices.append("Model: ").append(device.getModel()).append("\n");
            devices.append("Hostname: ").append(device.getHostName()).append("\n");
            devices.append("Ethernet Interfaces: ").append(device.getNumberOfEthernetInterfaces()).append("\n");
            devices.append("Configuration: ").append(device.getConfiguration()).append("\n");
            devices.append("-------------------\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Baza urządzeń");
        alert.setHeaderText("Lista urządzeń");
        alert.setContentText(devices.toString());
        alert.showAndWait();
    }
}
