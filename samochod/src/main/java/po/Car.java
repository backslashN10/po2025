package po;

public class Car {
	private boolean currentState;
	private int registrationNumber;
	private String model;
	private double maxSpeed;
	private Gearbox gearbox;
	private Engine engine;
	private Position position;
	private Position targetPosition;

	public Car(int registrationNumber, String model, double maxSpeed, Gearbox gearbox, Engine engine, Position position) {
		currentState = false;
		this.registrationNumber = registrationNumber;
		this.model = model;
		this.maxSpeed = maxSpeed;
		this.gearbox = gearbox;
		this.engine = engine;
		this.position = position;
		this.targetPosition = null;
	}
	public void start() {
		currentState = true;
		engine.start();
	}
	public void stop() {
		currentState = false;
		engine.stop();
	}
	public void driveTo(Position endPosition) {
		this.position.move(endPosition, getSpeed(), 0.016);
	}

	public void setTargetPosition(Position target) {
		this.targetPosition = target;
	}

	public Position getTargetPosition() {
		return targetPosition;
	}

	public void update() {
		if (targetPosition != null) {
			driveTo(targetPosition);
		}
	}
	public double getWeight() {
		return engine.getWeight() + gearbox.getWeight() + gearbox.getClutch().getWeight();
	}
	public double getSpeed() {
		return Math.min(engine.getRpm() * gearbox.getCurrentGearRatio(), maxSpeed);
	}
	public Position getPosition() {
		return position;
	}
	public int getRegistrationNumber() {
		return registrationNumber;
	}
	public String getModel() {
		return model;
	}
	public boolean getCurrentState() {
		return currentState;
	}
	public Gearbox getGearbox() {
		return gearbox;
	}
	public Engine getEngine() {
		return engine;
	}

	@Override
	public String toString() {
		return model;
	}
}
