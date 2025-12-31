package lab4;

import zadania.Zoo;

public class Main {
    public static void main(String[] args) {
        Zoo zoo = new Zoo();

        zoo.fillAnimals();

        System.out.println("Animals in the zoo:");
        zoo.showZoo();

        int totalLegs = zoo.countLegs();
        System.out.println("\nTotal legs in the zoo: " + totalLegs);
    }
}
