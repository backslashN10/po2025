package pl.edu.agh.po;

import java.util.Scanner;

public class Main {
    // for now this class is only for testing purpose
    public static void main(String[] args) {
        Utilities utilities = Utilities.getInstance();

        new Controller().start();
        utilities.backupDatabase();
    }
}
