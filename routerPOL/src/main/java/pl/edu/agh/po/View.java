package pl.edu.agh.po;

import java.util.Scanner;

public class View {
    private Scanner scanner = new Scanner(System.in);
    UserDAO userDAO = UserDAO.getInstance();
    AuthenticationService authService = AuthenticationService.getInstance();
    LoginData loginData = new LoginData();

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
    public LoginData showLoginProcess()
    {
        System.out.print("Login: ");
        String login = scanner.nextLine();
        System.out.print("Hasło: ");
        String password = scanner.nextLine();
        return new LoginData(login, password);
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
    public void showMenuSUDO()
    {
        System.out.println("=================================");
        System.out.println("UTWORZONO KONTO ADMINA");
        System.out.println("login: admin");
        System.out.println("hasło: " + userDAO.generateRandomPassword(12));
        System.out.println("=================================");
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
    public void showAddUser()
    {
        //adding user xd how to mozliwe
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
