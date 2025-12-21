package po;

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
			silnik.uruchom();
		}
		else {
			System.out.println("Samochód już jest włączony.");
		}
	}
	public void wylacz() {
		if (czyWlaczony == true) {
			czyWlaczony = false;
			silnik.zatrzymaj();
		}
		else {
			System.out.println("Samochód już jest wyłączony.");
		}
	}
	public void jedzDo(Pozycja pozycja) {
		this.pozycja.przemiesc(pozycja, getAktPredkosc(), 0.1);
	}
	public double getWaga() {
		return silnik.getWaga() + skrzyniaBiegow.getWaga() + skrzyniaBiegow.getSprzeglo().getWaga();
	}
	public double getAktPredkosc() {
		double predkoscTeoretyczna = silnik.getObroty() * skrzyniaBiegow.getAktPrzelozenie();
		double aktPredkosc;
		if (predkoscTeoretyczna > vMax) {
			aktPredkosc = vMax;
		}
		else {
			aktPredkosc = predkoscTeoretyczna;
		}
		return aktPredkosc;
	}
	public Pozycja getAktPozycja() {
		return pozycja;
	}
	public int getNrRejestracyjny() {
		return nrRejestracji;
	}
	public String getModel() {
		return model;
	}
	public boolean getCzyWlaczony() {
		return czyWlaczony;
	}
	public SkrzyniaBiegow getSkrzyniaBiegow() {
		return skrzyniaBiegow;
	}
	public Silnik getSilnik() {
		return silnik;
	}

	@Override
	public String toString() {
		return model + " (" + nrRejestracji + ")";
	}
}
