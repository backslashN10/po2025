	package zadania;
	
	import animals.Animal;
	import animals.Dog;
	import animals.Parrot;
	import animals.Snake;
	import java.util.Random;
	
	public class Zoo {
		private Animal[] animals = new Animal[100];
		
		public void fillAnimals() {
			Random rand = new Random();
			for (int i = 0; i < 100; i++) {
				int random_number = rand.nextInt(3);
				if (random_number == 0) {
					animals[i] = new Dog("dog"+i);
				}
				else if (random_number == 1) {
					animals[i] = new Parrot("parrot"+i);
				}
				else if (random_number == 2) {
					animals[i] = new Snake("snake"+i);
				}
			}
		}
		public int countLegs() {
			int counter = 0;
			for (int i = 0; i < 100; i++) {
				counter += animals[i].getLegs();
			}
			return counter;
		}
		public void showZoo() {
			for (int i = 0; i < 100; i++) {
				System.out.println(animals[i].getName());
			}
		}
	}
	
