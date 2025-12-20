package pl.edu.agh.po;

import org.fusesource.jansi.AnsiConsole;

import java.util.Scanner;

public class Controller
{

    //bo tak user bedzie null przy pierwszym wejsciu -> zrobic go adminem i dac pasy
    // gdy sie wyloguje xD no i co tera

    private boolean isRunning = true;
    AuthenticationService authService = AuthenticationService.getInstance();
    View view = new View();

    public void start()
    {
        while(isRunning)
        {
            AnsiConsole.systemInstall();
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
            case null:
                view.showMenuSUDO();
                view.showMenuLogin();
                break;
            default:
                view.defaultOption();

        }

    }



    public void handleUserLogin()
    {

        String username = scanner.nextLine();
        String password = scanner.nextLine();
        boolean success = authService.login(username, password);
        if (success) {
            System.out.println("Logowanie udane\n");
        } else {
            System.out.println("Logowanie nieudane.\n");
        }
    }


}
