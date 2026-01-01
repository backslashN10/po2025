package pl.edu.agh.po;

import java.util.List;
import java.util.Scanner;

public class View {
    private Scanner scanner = new Scanner(System.in);

    public int showMenuAdmin()
    {
        System.out.println(CLIStyle.blue("=== MENU ADMINA ==="));
        System.out.println(CLIStyle.green("1. Dodaj użytkownika"));
        System.out.println("2. Zablokuj użytkownika");
        System.out.println("3. Wyloguj");
        System.out.println(CLIStyle.red("0. Wyjście"));
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }
    public void showUsers(List<User> users) {
        System.out.println("=== Lista użytkowników ===");
        for (User user : users) {
            System.out.println(
                    "ID: " + user.getID() +
                            ", login: " + user.getUsername() +
                            ", rola: " + user.getRole()
            );
        }
    }
    public void showDevices(List<Device> devices)
    {
        System.out.println("=== Lista urządzeń ===");
        for(Device device : devices)
        {
            System.out.println("-------------------------------");
            System.out.println("ID: " + device.getId());
            System.out.println("Typ: " + device.getType());
            System.out.println("Status: " + device.getStatus());
            System.out.println("Model: " + device.getModel());
            System.out.println("Hostname: " + device.getHostName());
            System.out.println("Liczba interfejsów Ethernet: " + device.getNumberOfEthernetInterfaces());
            System.out.println("Konfiguracja: " + device.getConfiguration());
        }
        System.out.println("-------------------------------");
    }

    public void defaultOption()
    {
        System.out.println("Niepoprawna opcja");
    }
//    public String blockUser()
//    {
//        System.out.println("Podaj username kogo chcesz zablokowac");
//        String username = scanner.nextLine();
//        return username;
//
//    }
    public void showMessage(String message) {
        System.out.println(message);
    }
    private <T extends Enum<T>> T chooseEnum(String title, T[] values) {
        System.out.println("Wybierz " + title + ":");

        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i]);
        }

        int choice = -1;
        while (choice < 1 || choice > values.length) {
            System.out.print("Twój wybór: ");
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
            } else {
                scanner.next();
            }
        }
        scanner.nextLine(); // spalanie enter

        return values[choice - 1];
    }
    public DeviceType getType() {
        return chooseEnum("TYP", DeviceType.values());
    }

    public DeviceStatus getStatus() {
        return chooseEnum("STATUS", DeviceStatus.values());
    }

    public String getNewConfiguration() {
        System.out.println("Podaj nową konfigurację:");
        return scanner.nextLine();
    }

public long getDeviceId() {
    System.out.print("Podaj ID urządzenia: ");
    while (!scanner.hasNextLong()) {
        scanner.next();
        System.out.print("Podaj poprawne ID: ");
    }
    long id = scanner.nextLong();
    scanner.nextLine();
    return id;
}
    public Device getDataNewDevice()
    {
        DeviceType type = getType();
        DeviceStatus status = getStatus();

        System.out.print("Podaj model urządzenia: ");
        String model = scanner.nextLine();

        System.out.print("Podaj hostname: ");
        String hostname = scanner.nextLine();

        int ethernetCount = -1;
        while (ethernetCount < 0) {
            System.out.print("Podaj liczbę interfejsów Ethernet: ");
            if (scanner.hasNextInt()) {
                ethernetCount = scanner.nextInt();
            } else {
                scanner.next();
            }
        }
        scanner.nextLine();

        System.out.print("Podaj konfigurację urządzenia: ");
        String configuration = scanner.nextLine();

        return new Device(
                type,
                status,
                model,
                hostname,
                ethernetCount,
                configuration
        );
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
    public String getStringInput(String message)
    {
        System.out.println(message);
        String input = scanner.nextLine();
        return input.trim();
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
                scanner.next();
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

    public int showMenuTechnician()
    {
        System.out.println("=================================");
        System.out.println("Technician");
        System.out.println("1. Dodaj nowe Urządzenie");
        System.out.println("2. Zmień konfigurację ");
        System.out.println("3. Usuń urządzenie ");
        System.out.println("4. Pokaż bazę urządzeń ");
        System.out.println("5. Wyloguj");
        System.out.println("0. Wyjście");
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;

    }

}
