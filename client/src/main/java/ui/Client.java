package ui;

import java.util.Scanner;

import static java.lang.System.exit;

public class Client {
    private boolean firstPreLogin = true;
    private String authToken = null;
    private ServerFacade server;


    public void run(String hostname, int port) {
        server = new ServerFacade(hostname, port);
        System.out.println("Welcome to 240 Chess");

        while(authToken == null) {
            prelogin();
        }
    }
    private void printPreloginOptions() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Quit");
        System.out.println("4. Help");
        System.out.println("Please select an option");
    }

    private void prelogin() {
        if (firstPreLogin) {
            printPreloginOptions();
            firstPreLogin = false;
        }
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        switch(option) {
            case 1:
                RegisterUser();
                break;
            case 2:
                LoginUser();
                break;
            case 3:
                exit(0);
            case 4:
                printPreloginOptions();
        }
    }
    private void LoginUser() {
        System.out.println("Please enter your username:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        authToken = server.login(username, password);

    }
    private void RegisterUser() {
        System.out.println("Please enter your username:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        System.out.println("Please enter your email:");
        String email = scanner.nextLine();
        authToken = server.register(username, password,email);
    }
}
