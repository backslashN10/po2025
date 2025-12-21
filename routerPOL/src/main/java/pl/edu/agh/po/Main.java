package pl.edu.agh.po;


public class Main {
    public static void main(String[] args) {
        Utilities utilities = Utilities.getInstance();

        new Controller().start();
        utilities.backupDatabase();
    }
}
