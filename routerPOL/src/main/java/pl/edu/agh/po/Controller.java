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
    public void addUser() {

        LoginData loginData = view.getLoginProcess();
        String username = loginData.getUsername();
        String password = loginData.getPassword();
        UserRole role = view.getRole();

        User newUser = new User(username, password, role);
        userDAO.save(newUser); // zapis do repozytorium/listy
        System.out.println("Użytkownik dodany: " + newUser.getUsername());
    }
    public void showAllUsers() {
        List<User> users = userDAO.findALL();

        if (users.isEmpty()) {
            view.showMessage("Brak użytkowników w bazie.");
            return;
        }

        view.showUsers(users);
    }
    public void makeRaport()
    {
        businessManager.generateFullRaport();
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
                else if(input == 0) {
                    quit();
                }
                break;
            case TECHNICIAN:
                input =  view.showMenuTechnician();
                if(input == 1)
                {
                    view.addNewDevice();
                }else if(input == 2)
                {
                    view.changeConfig();
                }
                else if(input == 3)
                {
                    view.deleteDevice();
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
        String username = view.blockUser();
        User user = userDAO.findByUsername(username);
        if(user == null)
        {
            view.showMessage("Użytkownik o loginie " + username + " nie istnieje.");
            return;
        }
        userDAO.deleteByID(user.getID());
    }
    public void handleUserLogin()
    {
        LoginData data = view.getLoginProcess();
        boolean success = authService.login(data.getUsername(), data.getPassword());
        if (success) {
            System.out.println("Logowanie udane\n");
        } else {
            System.out.println("Logowanie nieudane.\n");
        }
    }

}
