package po;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewCarController {

    @FXML
    private TextField modelField;

    @FXML
    private TextField registrationField;

    @FXML
    private TextField maxSpeedField;

    @FXML
    private TextField maxRpmField;

    private Car createdCar;

    @FXML
    public void initialize() {
        createdCar = null;
    }

    @FXML
    private void handleAddCar() {
        try {
            String model = modelField.getText();
            if (model == null || model.trim().isEmpty()) {
                showError("Model nie może być pusty!");
                return;
            }
            String registrationText = registrationField.getText();
            if (registrationText == null || registrationText.trim().isEmpty()) {
                showError("Numer rejestracyjny nie może być pusty!");
                return;
            }
            int registrationNumber = Integer.parseInt(registrationText);
            String maxSpeedText = maxSpeedField.getText();
            if (maxSpeedText == null || maxSpeedText.trim().isEmpty()) {
                showError("Prędkość maksymalna nie może być pusta!");
                return;
            }
            double maxSpeed = Double.parseDouble(maxSpeedText);
            if (maxSpeed <= 0) {
                showError("Prędkość maksymalna musi być większa od 0!");
                return;
            }
            String maxRpmText = maxRpmField.getText();
            if (maxRpmText == null || maxRpmText.trim().isEmpty()) {
                showError("Maksymalne RPM nie może być puste!");
                return;
            }
            double maxRpm = Double.parseDouble(maxRpmText);
            if (maxRpm <= 0) {
                showError("Maksymalne RPM musi być większe od 0!");
                return;
            }

            Clutch clutch = new Clutch("Standard Clutch", 100, 1000);
            Gearbox gearbox = new Gearbox("Standard Gearbox", 200, 2000, 5, clutch,
                    new double[]{0.08, 0.09, 0.1, 0.11, 0.12, 0.13});
            Engine engine = new Engine("Standard Engine", 300, 3000, maxRpm);
            Position start = new Position(0, 0);
            createdCar = new Car(registrationNumber, model, maxSpeed, gearbox, engine, start);
            Stage stage = (Stage) modelField.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showError("Wprowadź poprawne wartości liczbowe!");
        } catch (Exception e) {
            showError("Błąd: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        createdCar = null;
        Stage stage = (Stage) modelField.getScene().getWindow();
        stage.close();
    }

    public Car getCreatedCar() {
        return createdCar;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
