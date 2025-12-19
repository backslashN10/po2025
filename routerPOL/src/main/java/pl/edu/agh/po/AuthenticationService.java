package pl.edu.agh.po;

import java.time.LocalDate;

public class AuthenticationService
{
    //User currentUser; klasa user do implementacji
    public static final String GREEN = "\u001B[32m";
    public static final String RESET = "\u001B[0m";

    public static void showWelcomeScreen() {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║          KORPORACYJNY SYSTEM ZARZĄDZANIA             ║");
        System.out.println("║               SPRZĘTEM SIECIOWYM v1.0                ║");
        System.out.println("║          CORPORATE DATABASE MANAGEMENT CLI           ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.print(  "╠  Status systemu : " + GREEN + "ONLINE" + RESET);
        System.out.println("                             ╣");
        System.out.print("║  Połączenie DB  : " + GREEN + "OK" + RESET);
        System.out.println("                                 ╣");
        System.out.println("║  Środowisko     : PRODUKCJA                          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
        //login
        //logout
        System.out.println("Zalogowany użytkownik: {uzupelnic}");
        System.out.println("Data: " + LocalDate.now());
        System.out.println();
    }
    //public User login()
    {
        //uchwyt do UserDAO
    }
    public void logout()
    {

    }
    //User getCurrentUser()
    //{
   //     return currentUser;
   // }
//    public boolean isLoggedIn()
//    {
//        return loggedStatus
//    }

    // DOPIERO PO AUTH, w zaleznosci od aktora
     public static void showMenuCEO() {
        System.out.println("────────────────────────────────────────────────────────");
        System.out.println("[1] Wyświetl bazę urządzeń(danych)");
        System.out.println("[2] Stwórz raport");
        System.out.println("[0] Wyjście");
        System.out.println("────────────────────────────────────────────────────────");
        System.out.print("Wybierz opcję: ");
    }
    public static void showMenuTechnician() {
        System.out.println("────────────────────────────────────────────────────────");
        System.out.println("[1] Wyświetl bazę urządzeń(danych)");
        System.out.println("[2] Dodaj nowe urządzenie");
        System.out.println("[3] Zmień konfigurację urządzenia");
        System.out.println("[4] Usuń urządzenie");
        System.out.println("[0] Wyjście");
        System.out.println("────────────────────────────────────────────────────────");
        System.out.print("Wybierz opcję: ");
    }
    public static void showMenuAdministrator() {
        System.out.println("────────────────────────────────────────────────────────");
        System.out.println("[1] Dodaj użytkownika");
        System.out.println("[2] Zablokuj użytkownika");
        System.out.println("[0] Wyjście");
        System.out.println("────────────────────────────────────────────────────────");
        System.out.print("Wybierz opcję: ");
    }
}

