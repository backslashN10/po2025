package po;

public class SkrzyniaBiegow extends Komponent {
	
	private int aktualnyBieg;
	private int iloscBiegow;
	private double aktualnePrzelozenie;
	public Sprzeglo sprzeglo;
	private double[] przelozenia;

	public SkrzyniaBiegow(String nazwa, double waga, double cena, int iloscBiegow, Sprzeglo sprzeglo, double[] przelozenia) {
		super(nazwa, waga, cena);
		this.iloscBiegow = iloscBiegow;
		this.sprzeglo = sprzeglo;
		aktualnyBieg = 0;
		if (przelozenia.length != iloscBiegow + 1) {
			System.out.println("Przełożeń musi być tyle co biegów + 1 (luz pod indeksem 0).");
		}
        this.przelozenia = przelozenia; 
		aktualnePrzelozenie = przelozenia[aktualnyBieg];
	}
	public void zwiekszBieg() {
		if (aktualnyBieg != iloscBiegow) {
			if (sprzeglo.czySprzegloLapie() == true) {
				aktualnyBieg += 1;
				aktualnePrzelozenie = przelozenia[aktualnyBieg];
			}
			else {
				System.out.println("Zmiana biegu wymaga sprzęgła.");
			}
		}
		else {
			System.out.println("Aktualnie w użyciu największy bieg.");
		}
	}
	public void zmniejszBieg() {
		if (aktualnyBieg != 0) {
			if (sprzeglo.czySprzegloLapie() == true) {
				aktualnyBieg -= 1;
				aktualnePrzelozenie = przelozenia[aktualnyBieg];
			}
			else {
				System.out.println("Zmiana biegu wymaga sprzęgła.");
			}
		}
		else {
			System.out.println("Aktualnie w użyciu najmniejszy bieg.");
		}
	}
	public int getAktBieg() {
		return aktualnyBieg;
	}
	public double getAktPrzelozenie() {
		return aktualnePrzelozenie;
	}
	public Sprzeglo getSprzeglo() {
		return sprzeglo;
	}

}
