package pl.edu.agh.po;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Hello world!
 */

public class App {
    public static String url = "jdbc:postgresql://localhost:5432/routerpol";
    public static String  user = "postgres";
    public static String password = "routerpol123";

    public static void main(String[] args) {


        obslugaProgramu();
    }
    //cala petla tylko zeby zobaczyc wstepne dzialanie interfejsu

    public static void obslugaProgramu()
    {

        Scanner scanner = new Scanner(System.in);
        String opcja;

        //routerpol123 do postgres
        //roch123 do roch


        //sqlite typowo lokalny wiec nie ma sensu, lepiej psql
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            // 1️⃣ Tworzymy tabelę, jeśli nie istnieje
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50),
                    age INT
                );
            """);

            // 2️⃣ Wstawiamy przykładowy rekord
            //stmt.executeUpdate("INSERT INTO users(name, age) VALUES('Jan', 25);");

            // 3️⃣ Wyświetlamy wszystkie rekordy
            ResultSet rs = stmt.executeQuery("SELECT * FROM users;");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getInt("age"));
            }
            stmt.executeUpdate("DELETE FROM users;");
            rs = stmt.executeQuery("SELECT * FROM users;");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("name") + " | " +
                        rs.getInt("age"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        do {
            AuthenticationService.showWelcomeScreen();
            System.out.println("wpisz opcje: ");
            System.out.println("1 - ceo: ");
            System.out.println("2 - technik: ");
            System.out.println("3 - administrator: ");

            opcja = scanner.nextLine();
            switch(opcja)
            {
                case "1":
                    AuthenticationService.showMenuCEO();
                    break;
                case "2":
                    AuthenticationService.showMenuTechnician();
                    break;
                case "3":
                    AuthenticationService.showMenuAdministrator();
                    break;
                default:
                    System.out.println("zle i huj");
            }


        }while(!opcja.equals("0"));


    }



}
