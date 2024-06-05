package ui;

import service.HTMLException;

import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Client {
    private boolean firstPreLogin = true;
    private boolean firstPostLogin = true;
    private String authToken = null;
    private ServerFacade server;


    public void run(String hostname, int port) {
        server = new ServerFacade(hostname, port);
        System.out.println("Welcome to 240 Chess");

        while(true) {
            prelogin();
            while(authToken != null) {
                postLogin();
            }

        }
    }
    private void printPreloginOptions() {
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Quit");
        System.out.println("4. Help");
        System.out.println("Please select an option");
    }

    private void printPostloginOptions() {
        System.out.println("1. Create Game");
        System.out.println("2. List Games");
        System.out.println("3. Join Game");
        System.out.println("4. Observe Game");
        System.out.println("5. Logout");
        System.out.println("6. Help");
        System.out.println("Please select an option");

    }

    private void prelogin() {
        if (firstPreLogin) {
            printPreloginOptions();
            firstPreLogin = false;
        }
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    registerUser();
                    System.out.println("User successfully registered");
                    break;
                case 2:
                    loginUser();
                    System.out.println("Login successful");
                    break;
                case 3:
                    exit(0);
                case 4:
                    printPreloginOptions();
                    break;
                case 5:
                    logout();
                default:
                    System.out.println("Invalid option, please enter a number between 1 and 4");

            }
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid input, please enter a number between 1 and 4");
        }

    }
    private void postLogin() {
        if (firstPostLogin) {
            printPostloginOptions();
            firstPostLogin = false;
        }
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    createGame();
                    System.out.println("Game created");
                    break;
                case 2:
                    listGames();
                    break;
                case 3:
                    joinGame(false);
                    System.out.println("Game joined");
                    break;
                case 4:
                    joinGame(true);
                    System.out.println("Game joined as observer");
                    break;
                case 5:
                    logout();
                    firstPostLogin = true;
                    firstPreLogin = true;
                    authToken = null;
                    System.out.println("Logged out successfully\n");
                    break;
                case 6:
                    printPostloginOptions();
                    break;
                default:
                    System.out.println("Invalid option, please enter a number between 1 and 6");

            }
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid input, please enter a number between 1 and 6");
        }

    }

    private void listGames() {
    }

    private void createGame() {
    }
    private void joinGame(boolean observerMode) {
    }
    private void logout() {
    }



    private void loginUser() {
        System.out.println("Please enter your username:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        try {
            authToken = server.login(username, password);
        }
        catch (HTMLException e) {
            System.out.println("Bad request: " + e.getMessage());
        }

    }
    private void registerUser() {
        System.out.println("Please enter your username:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        System.out.println("Please enter your email:");
        String email = scanner.nextLine();
        try {
            authToken = server.register(username, password,email);
        }
        catch (HTMLException e) {
            if (e.getErrorCode() == 403) {
                System.out.println("Already taken, please try again");
            }
            else {
                System.out.println("Bad request: " + e.getMessage());
            }

        }

    }
}
