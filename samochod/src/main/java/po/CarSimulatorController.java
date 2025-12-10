package po;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CarSimulatorController {

    @FXML
    private ComboBox<Samochod> carSelector;

    @FXML
    private Button addCarButton;

    @FXML
    private Label carModelLabel;

    @FXML
    private Label carRegistrationLabel;

    @FXML
    private Label carWeightLabel;

    @FXML
    private Label carSpeedLabel;

    @FXML
    private Label gearboxNameLabel;

    @FXML
    private Label gearboxPriceLabel;

    @FXML
    private Label gearboxWeightLabel;

    @FXML
    private Label gearboxGearLabel;

    @FXML
    private Label engineNameLabel;

    @FXML
    private Label enginePriceLabel;

    @FXML
    private Label engineWeightLabel;

    @FXML
    private Label engineRpmLabel;

    @FXML
    private Label clutchNameLabel;

    @FXML
    private Label clutchPriceLabel;

    @FXML
    private Label clutchWeightLabel;

    @FXML
    private Label clutchStateLabel;

    @FXML
    private StackPane visualizationArea;

    @FXML
    private Button turnOnButton;

    @FXML
    private Button turnOffButton;

    @FXML
    private Button action1Button;

    @FXML
    private Button action2Button;

    @FXML
    private Button action3Button;

    @FXML
    private Button action4Button;

    @FXML
    private RadioButton modeRadioButton;

    private Samochod samochod;
    private Sprzeglo sprzeglo;
    private SkrzyniaBiegow skrzynia;
    private Silnik silnik;

    @FXML
    public void initialize() {
        Sprzeglo sprzeglo1 = new Sprzeglo("sprzeglo1", 200, 757);
        SkrzyniaBiegow skrzynia1 = new SkrzyniaBiegow("skrzynia1", 100, 212, 2, sprzeglo1,
                new double[]{800, 1000, 3000});
        Silnik silnik1 = new Silnik("silnik1", 2431, 32, 5000);
        Pozycja start1 = new Pozycja(0, 0);
        Samochod auto1 = new Samochod(126, "Fiat 126p", 120, skrzynia1, silnik1, start1);

        Sprzeglo sprzeglo2 = new Sprzeglo("sprzeglo2", 300, 1200);
        SkrzyniaBiegow skrzynia2 = new SkrzyniaBiegow("skrzynia2", 150, 350, 5, sprzeglo2,
                new double[]{800, 1200, 2000, 3500, 5000, 7000});
        Silnik silnik2 = new Silnik("silnik2", 3500, 50, 8000);
        Pozycja start2 = new Pozycja(0, 0);
        Samochod auto2 = new Samochod(420, "Szybki zielony samochod", 280, skrzynia2, silnik2, start2);

        Sprzeglo sprzeglo3 = new Sprzeglo("sprzeglo3", 250, 900);
        SkrzyniaBiegow skrzynia3 = new SkrzyniaBiegow("skrzynia3", 120, 280, 6, sprzeglo3,
                new double[]{800, 1500, 2200, 3200, 4500, 6000, 8000});
        Silnik silnik3 = new Silnik("silnik3", 4200, 60, 9000);
        Pozycja start3 = new Pozycja(0, 0);
        Samochod auto3 = new Samochod(333, "wolny samochód czerwony", 250, skrzynia3, silnik3, start3);

        carSelector.getItems().addAll(auto1, auto2, auto3);
        carSelector.setValue(auto1);

        carSelector.setOnAction(e -> {
            Samochod wybranyAuto = carSelector.getValue();
            if (wybranyAuto != null) {
                samochod = wybranyAuto;
                skrzynia = samochod.getSkrzyniaBiegow();
                silnik = samochod.getSilnik();
                sprzeglo = skrzynia.getSprzeglo();
                modeRadioButton.setSelected(samochod.getCzyWlaczony());
                updateAllLabels();
            }
        });

        sprzeglo = sprzeglo1;
        skrzynia = skrzynia1;
        silnik = silnik1;
        samochod = auto1;

        modeRadioButton.setSelected(samochod.getCzyWlaczony());

        updateAllLabels();
    }

    private void updateAllLabels() {
        carModelLabel.setText(samochod.getModel());
        carRegistrationLabel.setText(String.valueOf(samochod.getNrRejestracyjny()));
        carWeightLabel.setText(String.format("%.1f kg", samochod.getWaga()));
        carSpeedLabel.setText(String.format("%.1f km/h", samochod.getAktPredkosc()));

        gearboxNameLabel.setText(skrzynia.getNazwa());
        gearboxPriceLabel.setText(String.format("%.0f zl", skrzynia.getCena()));
        gearboxWeightLabel.setText(String.format("%.0f kg", skrzynia.getWaga()));
        int aktBieg = skrzynia.getAktBieg();
        gearboxGearLabel.setText(aktBieg == 0 ? "N" : String.valueOf(aktBieg));

        engineNameLabel.setText(silnik.getNazwa());
        enginePriceLabel.setText(String.format("%.0f zl", silnik.getCena()));
        engineWeightLabel.setText(String.format("%.0f kg", silnik.getWaga()));
        engineRpmLabel.setText(String.format("%.0f RPM", silnik.getObroty()));

        clutchNameLabel.setText(sprzeglo.getNazwa());
        clutchPriceLabel.setText(String.format("%.0f zl", sprzeglo.getCena()));
        clutchWeightLabel.setText(String.format("%.0f kg", sprzeglo.getWaga()));
        clutchStateLabel.setText(String.format("%d%%", sprzeglo.getStanSprzegla()));
    }

    @FXML
    private void handleWlacz() {
        samochod.wlacz();
        modeRadioButton.setSelected(samochod.getCzyWlaczony());
        updateAllLabels();
    }

    @FXML
    private void handleWylacz() {
        samochod.wylacz();
        modeRadioButton.setSelected(samochod.getCzyWlaczony());
        updateAllLabels();
    }

    @FXML
    private void handleZwiekszBieg() {
        samochod.getSkrzyniaBiegow().zwiekszBieg();
        updateAllLabels();
    }

    @FXML
    private void handleZmniejszBieg() {
        skrzynia.zmniejszBieg();
        updateAllLabels();
    }

    @FXML
    private void handleAddCar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/NewCar.fxml")
            );
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dodaj nowy samochód");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSpeed(double speed) {
        carSpeedLabel.setText(String.format("%.1f km/h", speed));
    }

    public void updateRpm(double rpm) {
        engineRpmLabel.setText(String.format("%.0f RPM", rpm));
    }

    public void updateGear(int gear) {
        if (gear == 0) {
            gearboxGearLabel.setText("N");
        } else {
            gearboxGearLabel.setText(String.valueOf(gear));
        }
    }
}