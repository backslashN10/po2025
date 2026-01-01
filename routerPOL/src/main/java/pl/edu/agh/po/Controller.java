package pl.edu.agh.po;

import org.fusesource.jansi.AnsiConsole;

import java.util.List;

public class Controller
{

    private boolean isRunning = true;
    AuthenticationService authService = AuthenticationService.getInstance();
    View view = new View();
    UserDAO userDAO = UserDAO.getInstance();
    BusinessManager businessManager = BusinessManager.getInstance();
    DeviceDAO deviceDAO = DeviceDAO.getInstance();
    public void start()
    {
        AnsiConsole.systemInstall();
        while(isRunning)
        {
            handleShow();
        }
        AnsiConsole.systemUninstall();
    }
    public void quit()
    {
        isRunning = false;
    }
    public void addDevice() {
        Device newDevice = view.getDataNewDevice();
        deviceDAO.save(newDevice);
        view.showMessage("Urządzenie dodane poprawnie.");
    }
    public void addUser() {

        LoginData loginData = view.getLoginProcess();
        String username = loginData.getUsername();
        String password = loginData.getPassword();
        UserRole role = view.getRole();

        User newUser = new User(username, password, role);
        userDAO.save(newUser); // zapis do repozytorium/listy
        view.showMessage("Użytkownik dodany: " + newUser.getUsername());
    }
    public void showAllUsers() {
        List<User> users = userDAO.findALL();

        if (users.isEmpty()) {
            view.showMessage("Brak użytkowników w bazie.");
            return;
        }

        view.showUsers(users);
    }
    public void showAllDevices()
    {
        List<Device> devices = deviceDAO.findAll();
        if (devices.isEmpty()) {
            view.showMessage("Brak urządzeń w bazie.");
            return;
        }
        view.showDevices(devices);
    }

    public void makeRaport()
    {
        List<Device> devices = deviceDAO.findAll();
        if (devices.isEmpty()) {
            view.showMessage("Brak urządzeń w bazie. Nie można wygenerować raportu.");
            return;
        }
        businessManager.generateFullRaport();
        view.showMessage("Raport został wygenerowany.");
    }
    public void deleteDevice()
    {
        long id = view.getDeviceId();

        String typeInput = view.getStringInput("Podaj typ urządzenia (lub zostaw puste):");
        String statusInput = view.getStringInput("Podaj status urządzenia (lub zostaw puste):");

        //jesli nic nie wpisze to nie sprawdzamy poprawnosci, cecha nie interesuje
        //usera no a jak wpisze to chce sprawdzic czy sie zgadza i wtedy juz MUSI sie zgadzac
        DeviceStatus status = null;
        DeviceType type = null;

        try {
            if (typeInput != null && !typeInput.isEmpty()) {
                type = DeviceType.valueOf(typeInput.toUpperCase());
            }

            if (statusInput != null && !statusInput.isEmpty()) {
                status = DeviceStatus.valueOf(statusInput.toUpperCase());
            }

        } catch (IllegalArgumentException e) {
            view.showMessage("Błąd: Podano nieistniejący typ lub status!");
            return;
        }
        Device device = deviceDAO.findByID(id);
        if (device == null) {
            view.showMessage("Nie znaleziono urządzenia o ID: " + id);
            return;
        }
        if (type != null && !type.equals(device.getType())) {
            view.showMessage("Błąd! Urządzenie o ID " + id + " to " + device.getType() +
                    ", a nie " + type + ". Usuwanie anulowane.");
            return;
        }

        if (status != null && !status.equals(device.getStatus())) {
            view.showMessage("Błąd! Urządzenie ma status " + device.getStatus() +
                    ", a nie " + status + ". Usuwanie anulowane.");
            return;
        }

        deviceDAO.deleteByID(id);
        view.showMessage("Urządzenie zostało usunięte o ID: " + id);
    }
    public void changeDeviceConfiguration() {
        long id = view.getDeviceId();

        String typeInput = view.getStringInput("Podaj typ urządzenia (lub zostaw puste):");
        String statusInput = view.getStringInput("Podaj status urządzenia (lub zostaw puste):");

        //jesli nic nie wpisze to nie sprawdzamy poprawnosci, cecha nie interesuje
        //usera no a jak wpisze to chce sprawdzic czy sie zgadza i wtedy juz MUSI sie zgadzac
        DeviceStatus status = null;
        DeviceType type = null;

        try {
            if (typeInput != null && !typeInput.isEmpty()) {
                type = DeviceType.valueOf(typeInput.toUpperCase());
            }

            if (statusInput != null && !statusInput.isEmpty()) {
                status = DeviceStatus.valueOf(statusInput.toUpperCase());
            }

        } catch (IllegalArgumentException e) {
            view.showMessage("Błąd: Podano nieistniejący typ lub status!");
            return;
        }

        Device device = deviceDAO.findByID(id);
        if (device == null) {
            view.showMessage("Nie znaleziono urządzenia o ID: " + id);
            return;
        }
        if (type != null && !type.equals(device.getType())) {
            view.showMessage("Błąd! Urządzenie o ID " + id + " to " + device.getType() +
                    ", a nie " + type + ". Zmiana konfiguracji anulowana.");
            return;
        }

        if (status != null && !status.equals(device.getStatus())) {
            view.showMessage("Błąd! Urządzenie ma status " + device.getStatus() +
                    ", a nie " + status + ". Zmiana konfiguracji anulowana.");
            return;
        }
        String newConfiguration = view.getNewConfiguration();
        device.setConfiguration(newConfiguration);

        deviceDAO.updateData(device);

        view.showMessage("Konfiguracja urządzenia została zmieniona o ID: " + id);
    }


    public void handleShow()
    {
        int input = 666;
        User currentUser = authService.getCurrentUser();
        if(currentUser == null)
        {
                input = view.showMenuLogin();
                if(input == 1)
                {
                    handleUserLogin();
                }
                else if(input == 0) quit();
            return;
        }

        switch(authService.getCurrentUser().getRole())
        {
            case ADMIN:
                input = view.showMenuAdmin();
                if(input == 1)
                {
                    addUser();
                }
                else if(input == 2)
                {
                    blockUser();
                }
                else if(input == 3)
                {
                    authService.logout();
                }
                else if(input == 0)
                {
                    quit();
                }
                break;
            case CEO:
                input =  view.showMenuCEO();
                if(input == 1)
                {
                    showAllUsers();
                }
                else if(input == 2)
                {
                    makeRaport();
                }
                else if(input == 3)
                {
                    authService.logout();
                }
                else if(input == 0) {
                    quit();
                }
                break;
            case TECHNICIAN:
                input =  view.showMenuTechnician();
                if(input == 1)
                {
                   addDevice();
                }else if(input == 2)
                {
                    changeDeviceConfiguration();
                }
                else if(input == 3)
                {
                    deleteDevice();
                }
                else if(input == 4)
                {
                    showAllDevices();
                }
                else if(input == 5)
                {
                    authService.logout();
                }
                else if(input == 0)
                {
                    quit();
                }
                break;

            default:
                view.defaultOption();
        }
    }
    public void blockUser()
    {
        String username = view.getStringInput("Podaj login użytkownika do usunięcia.");
        String idInput = view.getStringInput("Podaj ID użytkownika (lub zostaw puste).");
        String roleInput = view.getStringInput("Podaj role użytkownika (lub zostaw puste");

        Long id = null;
        UserRole role = null;

        try
        {
            if(idInput != null && !idInput.isEmpty())
            {
                id = Long.parseLong(idInput);
            }
            if(roleInput != null && !roleInput.isEmpty())
            {
                role = UserRole.valueOf(roleInput.toUpperCase());
            }
        }catch(NumberFormatException e)
        {
            view.showMessage("BŁĄD : ID musi być liczbą.");
            return;
        }catch(IllegalArgumentException e)
        {
            view.showMessage("BŁĄD : Podano nieistniejącą rolę.");
            return;
        }

        User user = userDAO.findByUsername(username);
        if(user == null)
        {
            view.showMessage("Użytkownik o loginie " + username + " nie istnieje.");
            return;
        }

        if(id != null && !id.equals(user.getID()))
        {
            view.showMessage("Błąd! Login się zgadza, ale ID jest inne (Baza: " + user.getID() +
                    " vs Podane: " + id + "). Usuwanie anulowane.");
            return;
        }
        if(role != null && !role.equals(user.getRole()))
        {
            view.showMessage("Błąd! Login się zgadza, ale rola jest inna (Baza: " + user.getRole() +
                    " vs Podane: " + role + "). Usuwanie anulowane.");
            return;
        }

        view.showMessage("Użytkownik o loginie " + username + " został usunięty.");
        userDAO.deleteByID(user.getID());
    }
    public void handleUserLogin()
    {
        LoginData data = view.getLoginProcess();
        boolean success = authService.login(data.getUsername(), data.getPassword());
        if (success) {
            view.showMessage("Logowanie udane\n");
        } else {
            view.showMessage("Logowanie nieudane.\n");
        }
    }

}
