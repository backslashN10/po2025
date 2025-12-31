package po;

public class Clutch extends Component {
	private boolean currentClutchState;

	public Clutch(String name, double weight, double price) {
		super(name, weight, price);
		currentClutchState = false;
	}
	public void press() {
		currentClutchState = true;
	}
	public void release() {
		currentClutchState = false;
	}
	public boolean getCurrentClutchState() {
		return currentClutchState;
	}
}