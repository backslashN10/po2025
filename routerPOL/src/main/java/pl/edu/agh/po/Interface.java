package pl.edu.agh.po;

import org.fusesource.jansi.AnsiConsole;
import org.postgresql.core.Utils;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class Interface
{
    UserDAO userDAO = UserDAO.getInstance();
    private boolean isRunning = true;
    private Scanner scanner = new Scanner(System.in);

    public void start()
    {
        while(isRunning)
        {
            AnsiConsole.systemInstall();
            showMenu();
        }
        AnsiConsole.systemUninstall();
    }
    public void showMenu() {
        System.out.println("=================================");

        if (UserRole.ADMIN) {
            showMenuAdmin();
        } else if (UserRole.CEO) {
            showMenuCEO();
        } else if (UserRole.TECHNICIAN) {
            showMenuTechnician();
        }

        System.out.println("=================================");
    }
    private void handleInput() {
        if (UserRole.ADMIN)
        {
            handleInputAdmin();
        } else if (UserRole.CEO)
        {
            handleInputCEO();
        } else if (UserRole.TECHNICIAN)
        {
            handleInputTechnician();
        }
    }

    public void showMenuAdmin()
    {
        System.out.println(CLIStyle.blue("=== MENU ADMINA ==="));
        System.out.println(CLIStyle.green("1. Dodaj użytkownika"));
        System.out.println(CLIStyle.red("0. Wyjście"));
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
