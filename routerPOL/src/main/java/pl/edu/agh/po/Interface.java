package pl.edu.agh.po;

import java.util.Scanner;

public class Interface
{
    AuthenticationService authService = AuthenticationService.getInstance();

    UserDAO userDAO = UserDAO.getInstance();
    private boolean isRunning = true;
    private Scanner scanner = new Scanner(System.in);

    public void start()
    {
        while(isRunning)
        {
            showMenu();
        }
    }
    private void handleInput() {
        if (authService.isAdmin())
        {
            handleInputAdmin();
        } else if (authService.isCEO())
        {
            handleInputCEO();
        } else if (authService.isTechnician())
        {
            handleInputTechnician();
        }
    }

    public void showMenuAdmin()
    {
        System.out.println("=================================");
        System.out.println("ADMIN");
        System.out.println("1. Dodaj użytkownika");
        System.out.println("2. Zablokuj użytkownika");
        System.out.println("0. Wyjście");
        handleInputAdmin();

    }
    public void handleInputAdmin()
    {
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> addUser();
            case 2 -> blockUser();
            case 0 -> wyjscie();
            default -> System.out.println("Nieznana opcja");
        }
    }
    public void showMenuCEO()
    {
        System.out.println("=================================");
        System.out.println("CEO");
        System.out.println("1. Pokaż bazę danych");
        System.out.println("2. Stwórz raport ");
        System.out.println("0. Wyjście");
        handleInputCEO();

    }
    public void handleInputCEO()
    {
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> showDB();
            case 2 -> makeRaport();
            case 0 -> wyjazdZBudowy();
            default -> System.out.println("Nieznana opcja");
        }
    }
    public void showMenuTechnician()
    {
        System.out.println("=================================");
        System.out.println("Technician");
        System.out.println("1. Dodaj nowe Urządzenie");
        System.out.println("2. Zmień konfigurację ");
        System.out.println("3. Usuń urządzenie ");
        System.out.println("0. Wyjście");
        handleInputCEO();

    }
    public void handleInputTechnician()
    {
        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> addNewUrzadzenie();
            case 2 -> configChange();
            case 3 -> deleteUrzadzenie();
            case 0 -> siema();
            default -> System.out.println("Nieznana opcja");
        }
    }



    public void showMenuSUDO()
    {
        //tylko dla pierwszego uzytku(pierwszy obiekt konstruktora czy inny warunek na admina)
        System.out.println("=================================");
        System.out.println("UTWORZONO KONTO ADMINA");
        System.out.println("login: admin");
        System.out.println("hasło: " + userDAO.generateRandomPassword(12));
        System.out.println("=================================");
    }

}
