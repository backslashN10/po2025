package po;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class CarSimulatorController {
    
    // === SAMOCHOD ===
    @FXML
    private Label carModelLabel;
    
    @FXML
    private Label carRegistrationLabel;
    
    @FXML
    private Label carWeightLabel;
    
    @FXML
    private Label carSpeedLabel;
    
    // === SKRZYNIA BIEGOW ===
    @FXML
    private Label gearboxNameLabel;
    
    @FXML
    private Label gearboxPriceLabel;
    
    @FXML
    private Label gearboxWeightLabel;
    
    @FXML
    private Label gearboxGearLabel;
    
    // === SILNIK ===
    @FXML
    private Label engineNameLabel;
    
    @FXML
    private Label enginePriceLabel;
    
    @FXML
    private Label engineWeightLabel;
    
    @FXML
    private Label engineRpmLabel;
    
    // === SPRZEGLO ===
    @FXML
    private Label clutchNameLabel;
    
    @FXML
    private Label clutchPriceLabel;
    
    @FXML
    private Label clutchWeightLabel;
    
    @FXML
    private Label clutchStateLabel;
    
    // === WIZUALIZACJA ===
    @FXML
    private StackPane visualizationArea;
    
    // === TWOJE OBIEKTY ===
    private Samochod samochod;
    private Sprzeglo sprzeglo;
    private SkrzyniaBiegow skrzynia;
    private Silnik silnik;
    
    /**
     * Metoda wywolywana automatycznie po zaladowaniu FXML
     */
    @FXML
    public void initialize() {
        // Inicjalizacja obiektow (jak w Main.java)
        sprzeglo = new Sprzeglo("sprzeglo", 200, 757);
        skrzynia = new SkrzyniaBiegow("skrzynia", 100, 212, 2, sprzeglo, 
                                       new double[]{800, 1000, 3000});
        silnik = new Silnik("silnik", 2431, 32, 5000);
        Pozycja start = new Pozycja(0, 0);
        samochod = new Samochod(123, "cesna", 120, skrzynia, silnik, start);
        
        // Aktualizuj GUI
        updateAllLabels();
    }
    
    /**
     * Aktualizuje wszystkie etykiety danymi z obiektow
     */
    private void updateAllLabels() {
        // SAMOCHOD
        carModelLabel.setText("Cesna");
        carRegistrationLabel.setText("123");
        carWeightLabel.setText("1500 kg");
        carSpeedLabel.setText("0 km/h");
        
        // SKRZYNIA BIEGOW
        gearboxNameLabel.setText("Skrzynia");
        gearboxPriceLabel.setText("100 zl");
        gearboxWeightLabel.setText("212 kg");
        gearboxGearLabel.setText("N");
        
        // SILNIK
        engineNameLabel.setText("Silnik");
        enginePriceLabel.setText("2431 zl");
        engineWeightLabel.setText("32 kg");
        engineRpmLabel.setText("0 RPM");
        
        // SPRZEGLO
        clutchNameLabel.setText("Sprzeglo");
        clutchPriceLabel.setText("200 zl");
        clutchWeightLabel.setText("757 kg");
        clutchStateLabel.setText("100%");
    }
    
    /**
     * Obsluga przycisku "wlacz"
     */
    @FXML
    private void handleWlacz() {
        System.out.println("Silnik wlaczony!");
        // Tutaj dodaj logike wlaczania silnika
        // np. silnik.wlacz();
        updateAllLabels();
    }
    
    /**
     * Obsluga przycisku "wylacz"
     */
    @FXML
    private void handleWylacz() {
        System.out.println("Silnik wylaczony!");
        // Tutaj dodaj logike wylaczania silnika
        // np. silnik.wylacz();
        updateAllLabels();
    }
    
    /**
     * Metoda pomocnicza do aktualizacji predkosci
     */
    public void updateSpeed(double speed) {
        carSpeedLabel.setText(String.format("%.1f km/h", speed));
    }
    
    /**
     * Metoda pomocnicza do aktualizacji obrotow
     */
    public void updateRpm(double rpm) {
        engineRpmLabel.setText(String.format("%.0f RPM", rpm));
    }
    
    /**
     * Metoda pomocnicza do aktualizacji biegu
     */
    public void updateGear(int gear) {
        if (gear == 0) {
            gearboxGearLabel.setText("N");
        } else {
            gearboxGearLabel.setText(String.valueOf(gear));
        }
    }
}