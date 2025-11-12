package projekt_samochod;

public class Main {
	Sprzeglo sprzeglo = new Sprzeglo("sprzeglo", 200, 757);
	SkrzyniaBiegow skrzynia = new SkrzyniaBiegow("skrzynia", 100, 212, 2, sprzeglo, new double[]{800, 1000, 3000});
	Silnik silnik = new Silnik("silnik", 2431, 32, 5000);
	Pozycja start = new Pozycja(0,0);
	Samochod bryka = new Samochod(123, "cesna", 120, skrzynia, silnik, start);
}
