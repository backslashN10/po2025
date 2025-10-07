public class Choinka{
	public static void main(String[] args){
		if (args.length == 0){
			System.out.println("Number of branches is obligatory");
		}
		else{
			for (int i = 0; i < Integer.parseInt(args[0]); i++){
				for (int j = 0; j < i+1; j++){
					System.out.print("*");
				}
				System.out.print("\n");
			}
		}
	}
}
