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
		double epsilon = 0.01; // tolerancja dla porównań double
		
		while (Math.abs(x - pozycjaKoncowa.getX()) > epsilon || 
		       Math.abs(y - pozycjaKoncowa.getY()) > epsilon) {
			
			double deltaX = pozycjaKoncowa.getX() - x;
			double deltaY = pozycjaKoncowa.getY() - y;
			double odleglosc = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			
			// Normalizacja kierunku
			double nx = deltaX / odleglosc;
			double ny = deltaY / odleglosc;
			
			double krok = predkosc * dt;
			
			// Jeśli krok >= odległość, idź od razu do celu
			if (krok >= odleglosc) {
				x = pozycjaKoncowa.getX();
				y = pozycjaKoncowa.getY();
				break;
			}
			
			// Przesunięcie o krok w kierunku celu
			x = x + nx * krok;
			y = y + ny * krok;
		}
	}
}