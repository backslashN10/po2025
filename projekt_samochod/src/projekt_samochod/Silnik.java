package projekt_samochod;

public class Silnik extends Komponent {
	
	private double obroty;
	private double maxObroty;
	private boolean czyUruchomiony;

	public Silnik(String nazwa, double waga, double cena, double maxObroty) {
		super(nazwa, waga, cena);
		this.maxObroty = maxObroty;
		obroty = 0;
	}
	public void uruchom() {
		if (czyUruchomiony == false) {
			czyUruchomiony = true;
			obroty = 800;
			System.out.println("Silnik uruchomiony.");
		} else {
			System.out.println("Silnik już działa.");
		}
	}
	public void zatrzymaj() {
		if (czyUruchomiony == true) {
			czyUruchomiony = false;
			obroty = 0;
			System.out.println("Silnik zatrzymany.");
		} else {
			System.out.println("Silnik już jest wyłączony..");
		}
	}
	public void zwiekszObroty(double delta) {
		if (czyUruchomiony == true) {
			if (obroty+delta > maxObroty) {
				obroty = maxObroty;
			}
			else {
				obroty += delta;
			}
		}
		else {
			System.out.println("Silnik nie jest uruchomiony");
		}
	}
	public void zmniejszObroty(double delta) {
		if (czyUruchomiony == true) {
			if (obroty-delta < 0) {
				obroty = 0;
			}
			else {
				obroty -= delta;
			}
		}
		else {
			System.out.println("Silnik nie jest uruchomiony");
		}
	}
	public double getObroty(){
		return obroty;
	}
}

