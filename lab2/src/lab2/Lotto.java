package lab2;
import java.util.ArrayList;
import java.util.Random;

public class Lotto {
	public static void main(String[] args){
		ArrayList<Integer> user_numbers = new ArrayList<Integer>();
		for (int i = 0; i<6; i++) {
			user_numbers.add(Integer.parseInt(args[i]));
		}
		ArrayList<Integer> lottery_numbers = generate_numbers();
		ArrayList<Integer> common_numbers = (ArrayList<Integer>)lottery_numbers.clone();
		common_numbers.retainAll(user_numbers);
		System.out.print("lottery numbers: ");
		System.out.println(lottery_numbers);
		System.out.print("user numbers: ");
		System.out.println(user_numbers);
		System.out.print("number of matches: ");
		System.out.println(common_numbers.size());
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
