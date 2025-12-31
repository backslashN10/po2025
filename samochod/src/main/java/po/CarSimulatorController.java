package po;

import java.util.ArrayList;
import java.util.List;

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
    private AnimationTimer animationTimer;
    private Position targetPosition;
    private long lastUpdate;

    @FXML
    public void initialize() {
        carList = new ArrayList<>();
        Position start = new Position(0, 0);

        Clutch defaultClutch1 = new Clutch("defaultClutch1", 200, 777);
        Gearbox defaultGearbox1 = new Gearbox("defaultGearbox1", 200, 888, 2, defaultClutch1,
                new double[]{0.08, 0.09, 0.1});
        Engine defaultEngine1 = new Engine("defaultEngine1", 400, 999, 5000);
        Car defaultCar1 = new Car(126, "defaultCar1", 120, defaultGearbox1, defaultEngine1, start);

        Clutch defaultClutch2 = new Clutch("defaultClutch2", 300, 1200);
        Gearbox defaultGearbox2 = new Gearbox("defaultGearbox2", 150, 350, 5, defaultClutch2,
                new double[]{800, 1200, 2000, 3500, 5000, 7000});
        Engine defaultEngine2 = new Engine("defaultEngine2", 3500, 50, 8000);
        Car defaultCar2 = new Car(420, "defaultCar2", 280, defaultGearbox2, defaultEngine2, start);

        Clutch defaultClutch3 = new Clutch("defaultClutch3", 250, 900);
        Gearbox defaultGearbox3 = new Gearbox("defaultGearbox3", 120, 280, 6, defaultClutch3,
                new double[]{800, 1500, 2200, 3200, 4500, 6000, 8000});
        Engine defaultEngine3 = new Engine("defaultEngine3", 4200, 60, 9000);
        Car defaultCar3 = new Car(333, "defaultCar3", 250, defaultGearbox3, defaultEngine3, start);

        carList.add(defaultCar1);
        carList.add(defaultCar2);
        carList.add(defaultCar3);

        carSelector.getItems().addAll(carList);
        carSelector.setValue(defaultCar1);

        carSelector.setOnAction(e -> {
            Car selectedCar = carSelector.getValue();
            car = selectedCar;
            gearbox = car.getGearbox();
            engine = car.getEngine();
            clutch = gearbox.getClutch();
            modeRadioButton.setSelected(car.getCurrentState());
            updateAllLabels();
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

            targetPosition = new Position(worldX, worldY);
        });

        targetPosition = null;
        lastUpdate = 0;

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (targetPosition != null) {
                    car.driveTo(targetPosition);
                }

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
        //to do 
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
        targetPosition = new Position(0, 0);
    }
}