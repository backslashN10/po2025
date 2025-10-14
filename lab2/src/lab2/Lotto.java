package lab2;
import java.util.ArrayList;
import java.util.Random;

public class Lotto {
	public static void main(String[] args){
		long start_time = System.currentTimeMillis();
		ArrayList<Integer> user_random_numbers = new ArrayList<Integer>();
		ArrayList<Integer> lottery_numbers = generate_numbers();
		int counter = 0;
		while (!user_random_numbers.containsAll(lottery_numbers)) {
			user_random_numbers = generate_numbers();
			counter++;
		}
		long end_time = System.currentTimeMillis();
		System.out.println("lottery numbers: " + lottery_numbers);
		System.out.println("number of generations: " + counter);
		System.out.println("time elapsed: " + (end_time - start_time) + " ms");

}
	public static ArrayList<Integer> generate_numbers(){
		Random rand = new Random();
		ArrayList<Integer> lottery_numbers = new ArrayList<Integer>();
		while (lottery_numbers.size() != 6) {
			int new_number = rand.nextInt(45)+1;
			if (!lottery_numbers.contains(new_number)) {
				lottery_numbers.add(new_number);
			}
		}
		return lottery_numbers;
	}
}
