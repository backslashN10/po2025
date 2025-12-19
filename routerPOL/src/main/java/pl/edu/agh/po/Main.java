package pl.edu.agh.po;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AuthenticationService authService = AuthenticationService.getInstance();
        System.out.println("Baza danych zostala zainicjalizowana (plik: rp.db)");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Username (lub 'exit' aby wyjsc): ");
            String username = scanner.nextLine();

            if (username.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.print("Password: ");
            String password = scanner.nextLine();

            boolean success = authService.login(username, password);

            if (success) {
                System.out.println("✓ Logowanie udane! Znaleziono usera: " + username + "\n");
            } else {
                System.out.println("✗ Logowanie nieudane! Sprawdz username lub password.\n");
            }
        }

        scanner.close();
    }
}
