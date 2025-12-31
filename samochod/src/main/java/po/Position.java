package po;

public class Position {
	private double x;
	private double y;

	public Position(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public synchronized void move(Position targetPosition, double speed, double dt) {
		double epsilon = 2.0;
		double deltaX = targetPosition.getX() - x;
		double deltaY = targetPosition.getY() - y;
		double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		if (distance < epsilon) {
			x = targetPosition.getX();
			y = targetPosition.getY();
			return;
		}
		double nx = deltaX / distance;
		double ny = deltaY / distance;
		double step = speed * dt;
		if (step >= distance) {
			x = targetPosition.getX();
			y = targetPosition.getY();
		} else {
			x = x + nx * step;
			y = y + ny * step;
		}
	}
	public synchronized double getX() {
		return x;
	}
	public synchronized double getY() {
		return y;
	}
}