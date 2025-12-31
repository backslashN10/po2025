package po;

public class Engine extends Component {
	private double rpm;
	private double maxRpm;
	private boolean currentEngineState;

	public Engine(String name, double weight, double price, double maxRpm) {
		super(name, weight, price);
		this.maxRpm = maxRpm;
		rpm = 0;
	}
	public void start() {
		currentEngineState = true;
		rpm = 800;
	}
	public void stop() {
		currentEngineState = false;
		rpm = 0;
	}
	public void increaseRpm(double delta) {
		if (currentEngineState == true) {
			rpm = Math.min(rpm+delta, maxRpm);
		}
	}
	public void reduceRpm(double delta) {
		if (currentEngineState == true) {
			rpm = Math.max(rpm-delta, 800);
		}
	}
	public boolean getCurrentEngineState(){
		return currentEngineState;
	}
	public double getRpm(){
		return rpm;
	}
	public double getMaxRpm(){
		return maxRpm;
	}
}

