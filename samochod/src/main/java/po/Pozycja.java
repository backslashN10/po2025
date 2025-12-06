package po;

public class Pozycja {
	private double x;
	private double y;

	public Pozycja(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void przemiesc(Pozycja pozycjaKoncowa, double predkosc, double dt) {
		double epsilon = 0.01;

		while (Math.abs(x - pozycjaKoncowa.getX()) > epsilon ||
		       Math.abs(y - pozycjaKoncowa.getY()) > epsilon) {

			double deltaX = pozycjaKoncowa.getX() - x;
			double deltaY = pozycjaKoncowa.getY() - y;
			double odleglosc = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

			double nx = deltaX / odleglosc;
			double ny = deltaY / odleglosc;

			double krok = predkosc * dt;

			if (krok >= odleglosc) {
				x = pozycjaKoncowa.getX();
				y = pozycjaKoncowa.getY();
				break;
			}

			x = x + nx * krok;
			y = y + ny * krok;
		}
	}
}