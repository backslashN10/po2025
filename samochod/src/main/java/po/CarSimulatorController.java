package po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CarSimulatorController {

    @FXML
    private ComboBox<Car> carSelector;

    @FXML
    private Button addCarButton;

    @FXML
    private Label carModelLabel;

    @FXML
    private Label registrationNumberLabel;

    @FXML
    private Label carWeightLabel;

    @FXML
    private Label speedLabel;

    @FXML
    private Label gearboxNameLabel;

    @FXML
    private Label gearboxPriceLabel;

    @FXML
    private Label gearboxWeightLabel;

    @FXML
    private Label currentGearLabel;

    @FXML
    private Label engineNameLabel;

    @FXML
    private Label enginePriceLabel;

    @FXML
    private Label engineWeightLabel;

    @FXML
    private Label rpmLabel;

    @FXML
    private Label clutchNameLabel;

    @FXML
    private Label clutchPriceLabel;

    @FXML
    private Label clutchWeightLabel;

    @FXML
    private Label clutchStateLabel;

    @FXML
    private Pane visualizationArea;

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
    private Button tesButton;

    @FXML
    private RadioButton modeRadioButton;

    private Car car;
    private Clutch clutch;
    private Gearbox gearbox;
    private Engine engine;
    private Circle carIcon;
    private List<Car> carList;
    private Map<Car, CarSimulationThread> carThreads;
    private AnimationTimer animationTimer;

    @FXML
    public void initialize() {
        carList = new ArrayList<>();
        carThreads = new HashMap<>();

        Clutch defaultClutch1 = new Clutch("Standard Clutch", 200, 777);
        Gearbox defaultGearbox1 = new Gearbox("3-Speed Gearbox", 200, 888, 3, defaultClutch1,
                new double[]{0.005, 0.015, 0.025, 0.035});
        Engine defaultEngine1 = new Engine("City Engine", 400, 999, 5000);
        Car defaultCar1 = new Car(126, "City Car", 120, defaultGearbox1, defaultEngine1, new Position(0, 0));

        Clutch defaultClutch2 = new Clutch("Sport Clutch", 300, 1200);
        Gearbox defaultGearbox2 = new Gearbox("5-Speed Gearbox", 150, 350, 5, defaultClutch2,
                new double[]{0.005, 0.010, 0.018, 0.025, 0.032, 0.040});
        Engine defaultEngine2 = new Engine("Sport Engine", 3500, 50, 8000);
        Car defaultCar2 = new Car(420, "Sport Car", 280, defaultGearbox2, defaultEngine2, new Position(0, 0));

        Clutch defaultClutch3 = new Clutch("Racing Clutch", 250, 900);
        Gearbox defaultGearbox3 = new Gearbox("6-Speed Gearbox", 120, 280, 6, defaultClutch3,
                new double[]{0.005, 0.008, 0.014, 0.020, 0.027, 0.034, 0.042});
        Engine defaultEngine3 = new Engine("Racing Engine", 4200, 60, 9000);
        Car defaultCar3 = new Car(333, "Racing Car", 250, defaultGearbox3, defaultEngine3, new Position(0, 0));

        carList.add(defaultCar1);
        carList.add(defaultCar2);
        carList.add(defaultCar3);

        for (Car c : carList) {
            CarSimulationThread thread = new CarSimulationThread(c);
            carThreads.put(c, thread);
            thread.start();
        }

        carSelector.getItems().addAll(carList);
        carSelector.setValue(defaultCar1);

        carSelector.setOnAction(e -> {
            Car selectedCar = carSelector.getValue();
            System.out.println("[DEBUG] Switching to car: " + selectedCar.getModel() +
                              " at position (" + selectedCar.getPosition().getX() + ", " + selectedCar.getPosition().getY() + ")");
            car = selectedCar;
            gearbox = car.getGearbox();
            engine = car.getEngine();
            clutch = gearbox.getClutch();
            modeRadioButton.setSelected(car.getCurrentState());
            updateAllLabels();
            updateCarVisualization();
        });
        
        car = defaultCar1;
        gearbox = car.getGearbox();
        engine = car.getEngine();
        clutch = gearbox.getClutch();
        modeRadioButton.setSelected(car.getCurrentState());

        carIcon = new Circle(10, Color.RED);
        visualizationArea.getChildren().clear();
        visualizationArea.getChildren().add(carIcon);

        visualizationArea.setOnMouseClicked(event -> {
            double clickX = event.getX();
            double clickY = event.getY();

            double width = visualizationArea.getWidth();
            double height = visualizationArea.getHeight();

            double worldX = clickX - width / 2;
            double worldY = height / 2 - clickY;

            car.setTargetPosition(new Position(worldX, worldY));
        });

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateAllLabels();
                updateCarVisualization();
            }
        };
        animationTimer.start();

        updateAllLabels();
        updateCarVisualization();
    }

    private void updateAllLabels() {
        carModelLabel.setText(car.getModel());
        registrationNumberLabel.setText(String.valueOf(car.getRegistrationNumber()));
        carWeightLabel.setText(String.format("%.1f kg", car.getWeight()));
        speedLabel.setText(String.format("%.1f km/h", car.getSpeed()));

        gearboxNameLabel.setText(gearbox.getName());
        gearboxPriceLabel.setText(String.format("%.0f zl", gearbox.getPrice()));
        gearboxWeightLabel.setText(String.format("%.0f kg", gearbox.getWeight()));
        int currentGear = gearbox.getCurrentGear();
        currentGearLabel.setText(currentGear == 0 ? "N" : String.valueOf(currentGear));

        engineNameLabel.setText(engine.getName());
        enginePriceLabel.setText(String.format("%.0f zl", engine.getPrice()));
        engineWeightLabel.setText(String.format("%.0f kg", engine.getWeight()));
        rpmLabel.setText(String.format("%.0f RPM", engine.getRpm()));

        clutchNameLabel.setText(clutch.getName());
        clutchPriceLabel.setText(String.format("%.0f zl", clutch.getPrice()));
        clutchWeightLabel.setText(String.format("%.0f kg", clutch.getWeight()));
        clutchStateLabel.setText(clutch.getCurrentClutchState() ? "Wciśnięte" : "Zwolnione");

        modeRadioButton.setSelected(car.getCurrentState());
    }

private void updateCarVisualization() {
    double width = visualizationArea.getWidth();
    double height = visualizationArea.getHeight();

    if (width <= 0 || height <= 0) {
        return;
    }

    Position pos = car.getPosition();
    carIcon.setCenterX(width / 2 + pos.getX());
    carIcon.setCenterY(height / 2 - pos.getY());
}


    @FXML
    private void handleTurnOn() {
        car.start();
        updateAllLabels();
    }

    @FXML
    private void handleTurnOff() {
        car.stop();
        updateAllLabels();
    }

    @FXML
    private void handleIncreaseGear() {
        gearbox.increaseGear();
        updateAllLabels();
    }

    @FXML
    private void handleReduceGear() {
        gearbox.reduceGear();
        updateAllLabels();
    }

    @FXML
    private void handleAddCar() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/NewCar.fxml"));
            javafx.scene.Parent root = loader.load();

            NewCarController controller = loader.getController();

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Dodaj nowy samochód");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            Car newCar = controller.getCreatedCar();
            if (newCar != null) {
                carList.add(newCar);

                CarSimulationThread thread = new CarSimulationThread(newCar);
                carThreads.put(newCar, thread);
                thread.start();

                carSelector.getItems().add(newCar);
                carSelector.setValue(newCar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePressClutch() {
        clutch.press();
        updateAllLabels();
    }

    @FXML
    private void handleReleaseClutch() {
        clutch.release();
        updateAllLabels();
    }

    @FXML
    private void handleIncreaseRpm() {
        engine.increaseRpm(200);
        updateAllLabels();
        updateCarVisualization();
    }

    @FXML
    private void handleReduceRpm() {
        engine.reduceRpm(200);
        updateAllLabels();
        updateCarVisualization();
    }

    @FXML
    private void handleDriveTo() {
        car.setTargetPosition(new Position(0, 0));
    }
}