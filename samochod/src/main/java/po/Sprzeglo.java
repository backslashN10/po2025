package po;

public class Sprzeglo extends Komponent {
	private boolean czySprzegloWcisniete;

	public Sprzeglo(String nazwa, double waga, double cena) {
		super(nazwa, waga, cena);
		czySprzegloWcisniete = false;
	}
	public void wcisnij(int delta) {
		czySprzegloWcisniete = true;
	}
	public void zwolnij(int delta) {
		czySprzegloWcisniete = false;
	}
	public boolean getStanSprzegla() {
		return czySprzegloWcisniete;
	}
}