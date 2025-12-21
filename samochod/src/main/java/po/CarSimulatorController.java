package po;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CarSimulatorController {

    @FXML
    private ComboBox<Samochod> carSelector;

    @FXML
    private Button addCarButton;

    @FXML
    private Label modelSamochoduLabel;

    @FXML
    private Label nrRejestracjiLabel;

    @FXML
    private Label wagaSamochoduLabel;

    @FXML
    private Label predkoscLabel;

    @FXML
    private Label nazwaSkrzyniBiegowLabel;

    @FXML
    private Label cenaSkrzyniBiegowLabel;

    @FXML
    private Label wagaSkrzyniBiegowLabel;

    @FXML
    private Label aktualnyBiegLabel;

    @FXML
    private Label nazwaSilnikaLabel;

    @FXML
    private Label cenaSilnikaLabel;

    @FXML
    private Label wagaSilnikaLabel;

    @FXML
    private Label obrotyLabel;

    @FXML
    private Label nazwaSprzeglaLabel;

    @FXML
    private Label cenaSprzeglaLabel;

    @FXML
    private Label wagaSprzeglaLabel;

    @FXML
    private Label stanSprzeglaLabel;

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
    private ImageView ikonaSamochodu;

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

        // Stwórz ikonę samochodu (wizualizację)
        try {
            Image image = new Image(getClass().getResourceAsStream("/ikona.jpg"));
            ikonaSamochodu = new ImageView(image);
            ikonaSamochodu.setFitWidth(60);
            ikonaSamochodu.setFitHeight(60);
            ikonaSamochodu.setPreserveRatio(true);

            visualizationArea.getChildren().clear();
            visualizationArea.getChildren().add(ikonaSamochodu);
        } catch (Exception e) {
            System.out.println("Błąd ładowania ikony samochodu: " + e.getMessage());
            e.printStackTrace();
        }

        updateAllLabels();
        updateCarVisualization();
    }

    private void updateAllLabels() {
        modelSamochoduLabel.setText(samochod.getModel());
        nrRejestracjiLabel.setText(String.valueOf(samochod.getNrRejestracyjny()));
        wagaSamochoduLabel.setText(String.format("%.1f kg", samochod.getWaga()));
        predkoscLabel.setText(String.format("%.1f km/h", samochod.getAktPredkosc()));

        nazwaSkrzyniBiegowLabel.setText(skrzynia.getNazwa());
        cenaSkrzyniBiegowLabel.setText(String.format("%.0f zl", skrzynia.getCena()));
        wagaSkrzyniBiegowLabel.setText(String.format("%.0f kg", skrzynia.getWaga()));
        int aktBieg = skrzynia.getAktBieg();
        aktualnyBiegLabel.setText(aktBieg == 0 ? "N" : String.valueOf(aktBieg));

        nazwaSilnikaLabel.setText(silnik.getNazwa());
        cenaSilnikaLabel.setText(String.format("%.0f zl", silnik.getCena()));
        wagaSilnikaLabel.setText(String.format("%.0f kg", silnik.getWaga()));
        obrotyLabel.setText(String.format("%.0f RPM", silnik.getObroty()));

        nazwaSprzeglaLabel.setText(sprzeglo.getNazwa());
        cenaSprzeglaLabel.setText(String.format("%.0f zl", sprzeglo.getCena()));
        wagaSprzeglaLabel.setText(String.format("%.0f kg", sprzeglo.getWaga()));
        stanSprzeglaLabel.setText(sprzeglo.getStanSprzegla() ? "Wciśnięte" : "Zwolnione");
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
        predkoscLabel.setText(String.format("%.1f km/h", speed));
    }

    public void updateRpm(double rpm) {
        obrotyLabel.setText(String.format("%.0f RPM", rpm));
    }

    public void updateGear(int gear) {
        if (gear == 0) {
            aktualnyBiegLabel.setText("N");
        } else {
            aktualnyBiegLabel.setText(String.valueOf(gear));
        }
    }

    private void updateCarVisualization() {
        if (ikonaSamochodu == null) return;

        Pozycja poz = samochod.getAktPozycja();
        // Centruj ikonę w visualizationArea i dodaj offset na podstawie pozycji
        double width = visualizationArea.getWidth() > 0 ? visualizationArea.getWidth() : 400;
        double height = visualizationArea.getHeight() > 0 ? visualizationArea.getHeight() : 400;
        double centerX = width / 2;
        double centerY = height / 2;

        // Ustaw pozycję (ImageView używa layoutX/Y a nie centerX/Y)
        ikonaSamochodu.setLayoutX(centerX + poz.getX() - ikonaSamochodu.getFitWidth() / 2);
        ikonaSamochodu.setLayoutY(centerY - poz.getY() - ikonaSamochodu.getFitHeight() / 2); // Odwróć Y, bo JavaFX ma Y w dół

        System.out.println("Pozycja samochodu: (" + poz.getX() + ", " + poz.getY() + ")");
        System.out.println("Pozycja ikony: (" + ikonaSamochodu.getLayoutX() + ", " + ikonaSamochodu.getLayoutY() + ")");
    }

    @FXML
    private void handleWcisnijSprzeglo() {
        sprzeglo.wcisnij(0);
        updateAllLabels();
    }

    @FXML
    private void handleZwolnijSprzeglo() {
        sprzeglo.zwolnij(0);
        updateAllLabels();
    }

    @FXML
    private void handleGaz() {
        silnik.zwiekszObroty(200);
        updateAllLabels();
        updateCarVisualization();
    }

    @FXML
    private void handleHamuj() {
        silnik.zmniejszObroty(200);
        updateAllLabels();
        updateCarVisualization();
    }

    @FXML
    private void handleJedzDo() {
        // Testowa metoda - jedź do pozycji (100, 100)
        Pozycja cel = new Pozycja(100, 100);
        samochod.jedzDo(cel);
        updateAllLabels();
        updateCarVisualization();
    }
}