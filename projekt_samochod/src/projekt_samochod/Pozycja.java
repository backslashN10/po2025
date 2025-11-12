package projekt_samochod;

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
		while (x != pozycjaKoncowa.getX() && y != pozycjaKoncowa.getY()) {
			double deltaX = pozycjaKoncowa.getX() - x;
			double deltaY = pozycjaKoncowa.getY() - y;
			double krok = predkosc * dt;
			x = x + deltaX/krok;
			y = y + deltaY/krok;
		}
	}

}
