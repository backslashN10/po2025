package lab2;

public class CodingBat {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	public boolean nearHundred(int n) {
		  if (Math.abs(100-n) <= 10 || Math.abs(200-n) <= 10){
		    return true;
		  }
		  return false;
		}

}
