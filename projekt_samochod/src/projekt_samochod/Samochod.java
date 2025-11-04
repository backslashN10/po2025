package projekt_samochod;

public class Samochod {
	private boolean czyWlaczony;
	private int nrRejestracji;
	private String model;
	private double vMax;
	private SkrzyniaBiegow skrzyniaBiegow;
	private Silnik silnik;
	private Pozycja pozycja;

	public Samochod(int nrRejestracji, String model, double vMax, SkrzyniaBiegow skrzyniaBiegow, Silnik silnik, Pozycja pozycja) {
		czyWlaczony = false;
		this.nrRejestracji = nrRejestracji;
		this.model = model;
		this.vMax = vMax;
		this.skrzyniaBiegow = skrzyniaBiegow;
		this.silnik = silnik;
		this.pozycja = pozycja;
	}
	public void wlacz() {
		if (czyWlaczony == false) {
			czyWlaczony = true;
		}
		else {
			System.out.println("Samochód już jest włączony.");
		}
	}
	public void wylacz() {
		if (czyWlaczony == true) {
			czyWlaczony = false;
		}
		else {
			System.out.println("Samochód już jest wyłączony.");
		}
	}
	public void jedzDo(Pozycja pozycja) {
		this.pozycja = pozycja;
	}
	public void jedzOWektor()
}
