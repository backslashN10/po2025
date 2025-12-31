package po;

public class Gearbox extends Component {
	private int currentGear;
	private int numberOfGears;
	private double currentGearRatio;
	public Clutch clutch;
	private double[] gearRatios;

	public Gearbox(String name, double weight, double price, int numberOfGears, Clutch clutch, double[] gearRatios) {
		super(name, weight, price);
		this.numberOfGears = numberOfGears;
		this.clutch = clutch;
		currentGear = 0;
        this.gearRatios = gearRatios; 
		currentGearRatio = gearRatios[currentGear];
	}
	public void increaseGear() {
		if (clutch.getCurrentClutchState()){
			currentGear = Math.min(currentGear+1, numberOfGears);
			currentGearRatio = gearRatios[currentGear];
		}
	}
	public void reduceGear() {
		if (clutch.getCurrentClutchState()){
			currentGear = Math.max(currentGear-1, 0);
			currentGearRatio = gearRatios[currentGear];
		}
	}
	public int getCurrentGear() {
		return currentGear;
	}

	public int getNumberOfGears() {
		return numberOfGears;
	}

	public double getCurrentGearRatio() {
		return currentGearRatio;
	}

	public Clutch getClutch() {
		return clutch;
	}

	public double[] getGearRatios() {
		return gearRatios;
	}

}
