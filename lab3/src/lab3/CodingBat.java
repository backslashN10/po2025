package lab3;

public class CodingBat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public boolean sleepIn(boolean weekday, boolean vacation) {
		  if (!weekday || vacation){
		    return true;
		  }
		  else{
		    return false;
		  }
	}
	public int diff21(int n) {
		  if (n>21){
		    return 2*Math.abs(n-21);
		  }
		  else{
		    return Math.abs(n-21);
		  }
	}
	public String helloName(String name) {
		  return "Hello "+name+"!";
		}
	public int countEvens(int[] nums) {
		  int counter = 0;
		  for (int n: nums){
		    if (n%2 == 0){
		      counter ++;
		    }
		  }
		  return counter;
		}



}
