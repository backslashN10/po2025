package pl.edu.agh.po;

import org.fusesource.jansi.AnsiConsole;

import java.util.Scanner;

public class Controller
{

    private boolean isRunning = true;
    AuthenticationService authService = AuthenticationService.getInstance();
    View view = new View();
    UserDAO userDAO = UserDAO.getInstance();

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
                if(input == 0)
                {
                    view.showAddUser();
                }
                else if(input == 1)
                {
                    view.blockUser();
                }
                else if(input == 2)
                {
                    quit();
                }
                break;
            case CEO:
                input =  view.showMenuCEO();
                if(input == 1)
                {
                    view.showDB();
                }
                else if(input == 2)
                {
                    view.makeRaport();
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
    public void handleAdminPassword()
    {
        view.showMenuSUDO("admin1");

    }
    public void handleUserLogin()
    {
        LoginData data = view.showLoginProcess();
        boolean success = authService.login(data.getUsername(), data.getPassword());
        if (success) {
            System.out.println("Logowanie udane\n");
        } else {
            System.out.println("Logowanie nieudane.\n");
        }
    }

}
