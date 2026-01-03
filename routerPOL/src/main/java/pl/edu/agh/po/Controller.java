package pl.edu.agh.po;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.util.Pair;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.input.Clipboard;

public class Controller {

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

    private final AuthenticationService authService = AuthenticationService.getInstance();
    private final UserDAO userDAO = UserDAO.getInstance();
    private final DeviceDAO deviceDAO = DeviceDAO.getInstance();
    private final BusinessManager businessManager = BusinessManager.getInstance();
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

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (authService.login(username, password)) {
            loginMessageLabel.setText("");
            logger.info("user: " + username + " (" + authService.getCurrentUser().getRole() + ") logged in");
            switch (authService.getCurrentUser().getRole()) {
                case ADMIN:
                    showAdminPanel();
                    break;
                case CEO:
                    showCeoPanel();
                    break;
                case TECHNICIAN:
                    showTechnicianPanel();
                    break;
            }
            usernameField.clear();
            passwordField.clear();
        } else {
            logger.warning("user provided bad credentials");
            passwordField.clear();
            loginMessageLabel.setText("Invalid credentials!");
        }
    }

    @FXML
    private void handleLogout() {
        logger.info("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") logged out");
        authService.logout();
        showLoginPanel();
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
            User newUser = new User(credentials.getKey(), credentials.getValue(), roleChoiceBox.getValue());
            userDAO.save(newUser);
            logger.info("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") added new user to database with ID: " + newUser.getID());

        });
    }

    @FXML
    private void handleBlockUser() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Usuń użytkownika");
        dialog.setContentText("Username:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(username -> {
            User user = userDAO.findByUsername(username);
            if (user != null) {
                userDAO.deleteByID(user.getID());
                logger.info("user: " + authService.getCurrentUser().getUsername() + " (" + authService.getCurrentUser().getRole() + ") deleted user with ID: " + user.getID());
            }
        });
    }

    @FXML
    private void handleShowDatabase() {
        StringBuilder usersDB = new StringBuilder();
        for (User user : userDAO.findALL()) {
            usersDB.append("ID: ").append(user.getID()).append("\n");
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
        String report = businessManager.generateFullRaport();
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
                deviceDAO.save(device);
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
                Device device = deviceDAO.findByID(id);
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
                        device.setConfiguration(config);
                        deviceDAO.updateData(device);
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
                deviceDAO.deleteByID(id);
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

        for (Device device : deviceDAO.findAll()) {
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
