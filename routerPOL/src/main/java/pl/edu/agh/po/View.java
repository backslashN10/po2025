package pl.edu.agh.po;

import java.util.Scanner;

public class View {
    private Scanner scanner = new Scanner(System.in);

    public int showMenuAdmin()
    {
        System.out.println(CLIStyle.blue("=== MENU ADMINA ==="));
        System.out.println(CLIStyle.green("1. Dodaj użytkownika"));
        System.out.println("2. Zablokuj użytkownika");
        System.out.println(CLIStyle.red("0. Wyjście"));
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
    public int showDB()
    {
        //show db in some way arraylist or as you want
        return 0;
    }
    public void defaultOption()
    {
        System.out.println("Niepoprawna opcja");
    }
    public void blockUser()
    {
        //blocking
    }
    public void addNewDevice()
    {
        //some formula to Device object
    }
    public int showMenuLogin()
    {
        System.out.println(CLIStyle.blue("=== SIEMA ==="));
        System.out.println(CLIStyle.green("1. Zaloguj się"));
        System.out.println(CLIStyle.red("0. Wyjście"));
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
    public LoginData getLoginProcess()
    {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Hasło: ");
        String password = scanner.nextLine();
        return new LoginData(login, password);
    }
    public UserRole getRole() {
        System.out.println("Wybierz rolę:");
        UserRole[] roles = UserRole.values();
        for (int i = 0; i < roles.length; i++) {
            System.out.println((i + 1) + ". " + roles[i]);
        }

        int choice = -1;
        while (choice < 1 || choice > roles.length) {
            System.out.print("Twój wybór: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next(); // ignoruje złe dane
            }
        }
        scanner.nextLine();
        return roles[choice - 1];
    }
    public int showMenuCEO()
    {
        System.out.println("=================================");
        System.out.println("CEO");
        System.out.println("1. Pokaż bazę danych");
        System.out.println("2. Stwórz raport ");
        System.out.println("0. Wyjście");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
    public int makeRaport()
    {
        System.out.println("1. Raport miesięczny");
        System.out.println("2. Raport roczny");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public int showMenuTechnician()
    {
        System.out.println("=================================");
        System.out.println("Technician");
        System.out.println("1. Dodaj nowe Urządzenie");
        System.out.println("2. Zmień konfigurację ");
        System.out.println("3. Usuń urządzenie ");
        System.out.println("0. Wyjście");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;

    }

    public void changeConfig()
    {
        //also some formula

    }
    public void deleteDevice()
    {
        //some formula
    }

}
