package po;

public class Sprzeglo extends Komponent {
	private int stanSprzegla;
	private int skokSprzegla;

	public Sprzeglo(String nazwa, double waga, double cena) {
		super(nazwa, waga, cena);
		stanSprzegla = 0;
	}
	public void wcisnij(int delta) {
		if (stanSprzegla+delta > 100) {
			stanSprzegla = 100;
		}
		else {
			stanSprzegla += delta;
		}
	}
	public void zwolnij(int delta) {
		if (stanSprzegla-delta < 0) {
			stanSprzegla = 0;
		}
		else {
			stanSprzegla -= delta;
		}
	}
	public boolean czySprzegloLapie() {
		return stanSprzegla >= skokSprzegla;
	}
	public int getStanSprzegla() {
		return stanSprzegla;
	}
}
