package po;

import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
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
    private RadioButton clutch1Radio;

    @FXML
    private RadioButton clutch2Radio;

    @FXML
    private RadioButton clutch3Radio;

    @FXML
    private TextField customClutchName;

    @FXML
    private TextField customClutchPrice;

    @FXML
    private TextField customClutchWeight;

    @FXML
    private RadioButton gearbox1Radio;

    @FXML
    private RadioButton gearbox2Radio;

    @FXML
    private RadioButton gearbox3Radio;

    @FXML
    private TextField customGearboxName;

    @FXML
    private TextField customGearboxPrice;

    @FXML
    private TextField customGearboxWeight;

    @FXML
    private TextField customGearboxGears;

    @FXML
    private TextField customGearboxRatios;

    @FXML
    private RadioButton engine1Radio;

    @FXML
    private RadioButton engine2Radio;

    @FXML
    private RadioButton engine3Radio;

    @FXML
    private TextField customEngineName;

    @FXML
    private TextField customEnginePrice;

    @FXML
    private TextField customEngineWeight;

    @FXML
    private TextField customEngineMaxRpm;

    @FXML
    public void initialize() {
    }

    @FXML
    private void handleAddCar() {
        try {
            String model = modelField.getText();
            int nrRejestracji = Integer.parseInt(registrationField.getText());
            double vMax = Double.parseDouble(maxSpeedField.getText());

            Sprzeglo sprzeglo = createClutch();
            SkrzyniaBiegow skrzynia = createGearbox(sprzeglo);
            Silnik silnik = createEngine();

            Pozycja start = new Pozycja(0, 0);
            Samochod nowySamochod = new Samochod(nrRejestracji, model, vMax, skrzynia, silnik, start);

            System.out.println("Utworzono nowy samochód: " + nowySamochod);

            Stage stage = (Stage) modelField.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            System.err.println("Błąd podczas tworzenia samochodu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Sprzeglo createClutch() {
        if (clutch1Radio.isSelected()) {
            return new Sprzeglo("sprzeglo1", 200, 757);
        } else if (clutch2Radio.isSelected()) {
            return new Sprzeglo("sprzeglo2", 300, 1200);
        } else if (clutch3Radio.isSelected()) {
            return new Sprzeglo("sprzeglo3", 250, 900);
        } else {
            String nazwa = customClutchName.getText();
            double cena = Double.parseDouble(customClutchPrice.getText());
            double waga = Double.parseDouble(customClutchWeight.getText());
            return new Sprzeglo(nazwa, cena, waga);
        }
    }

    private SkrzyniaBiegow createGearbox(Sprzeglo sprzeglo) {
        if (gearbox1Radio.isSelected()) {
            return new SkrzyniaBiegow("skrzynia1", 100, 212, 2, sprzeglo,
                    new double[]{800, 1000, 3000});
        } else if (gearbox2Radio.isSelected()) {
            return new SkrzyniaBiegow("skrzynia2", 150, 350, 5, sprzeglo,
                    new double[]{800, 1200, 2000, 3500, 5000, 7000});
        } else if (gearbox3Radio.isSelected()) {
            return new SkrzyniaBiegow("skrzynia3", 120, 280, 6, sprzeglo,
                    new double[]{800, 1500, 2200, 3200, 4500, 6000, 8000});
        } else {
            String nazwa = customGearboxName.getText();
            double cena = Double.parseDouble(customGearboxPrice.getText());
            double waga = Double.parseDouble(customGearboxWeight.getText());
            int liczbaBiegow = Integer.parseInt(customGearboxGears.getText());

            String[] ratiosStr = customGearboxRatios.getText().split(",");
            double[] przelozenia = new double[ratiosStr.length];
            for (int i = 0; i < ratiosStr.length; i++) {
                przelozenia[i] = Double.parseDouble(ratiosStr[i].trim());
            }

            return new SkrzyniaBiegow(nazwa, cena, waga, liczbaBiegow, sprzeglo, przelozenia);
        }
    }

    private Silnik createEngine() {
        if (engine1Radio.isSelected()) {
            return new Silnik("silnik1", 2431, 32, 5000);
        } else if (engine2Radio.isSelected()) {
            return new Silnik("silnik2", 3500, 50, 8000);
        } else if (engine3Radio.isSelected()) {
            return new Silnik("silnik3", 4200, 60, 9000);
        } else {
            String nazwa = customEngineName.getText();
            double cena = Double.parseDouble(customEnginePrice.getText());
            double waga = Double.parseDouble(customEngineWeight.getText());
            double maxRpm = Double.parseDouble(customEngineMaxRpm.getText());
            return new Silnik(nazwa, cena, waga, maxRpm);
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) modelField.getScene().getWindow();
        stage.close();
    }
}
