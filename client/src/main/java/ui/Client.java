package ui;

import model.GameData;
import service.HTMLException;

import java.util.Collection;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class Client {
    private boolean firstPreLogin = true;
    private boolean firstPostLogin = true;
    private String authToken = null;
    private ServerFacade server;
    HashMap<Integer,Integer> gameMap = new HashMap<Integer,Integer>();


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
                    break;
                case 2:
                    loginUser();
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
        try {
            Collection<GameData> games = server.listGames(authToken);
            printGameList(games);
        }
        catch (HTMLException e) {
            System.out.println("Bad request: " + e.getMessage());
        }
    }

    private void createGame() {
        System.out.println("Enter a name for the new game:");
        Scanner scanner = new Scanner(System.in);
        String gameName = scanner.nextLine();
        try {
            server.createGame(gameName, authToken);
            System.out.println("Game created");
        }
        catch (HTMLException e) {
            System.out.println("Bad request: " + e.getMessage());
        }
    }
    private void joinGame(boolean observerMode) {
        System.out.println("Select a game to join:");
        Scanner scanner = new Scanner(System.in);
        int gameSelect = 0;
        try {
            gameSelect = scanner.nextInt();
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid input, please enter a valid game number");
        }
        gameSelect = gameMap.getOrDefault(gameSelect, 0);
        if (gameSelect == 0) {
            System.out.println("Invalid input, please enter a valid game number");
        }
        else {
            System.out.println("Select 1 for white or 2 for black:");
            int colorSelect = 0;
            try {
                colorSelect = scanner.nextInt();
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input, Select 1 for white or 2 for black:");
            }
            if (colorSelect != 1 && colorSelect != 2) {
                System.out.println("Invalid input, Select 1 for white or 2 for black:");
            }
            try {
                server.joinGame(gameSelect, colorSelect, authToken);
                System.out.println("Game joined");
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
    private void logout() {
        try {
            server.logout(authToken);
            firstPostLogin = true;
            firstPreLogin = true;
            authToken = null;
            System.out.println("Logged out successfully\n");
        }
        catch (HTMLException e) {
            System.out.println("Logout unsuccessful, please try again: " + e.getMessage());
        }
    }



    private void loginUser() {
        System.out.println("Please enter your username:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        try {
            authToken = server.login(username, password);
            System.out.println("Login successful");
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
            System.out.println("User successfully registered");
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
    private void printGameList(Collection<GameData> games) {
        int i = 0;
        for (var game : games) {
            i++;
            gameMap.put(i,game.gameID());
            System.out.print(i + ". " + game.gameName() + "\n   White: ");
            if (game.whiteUsername() != null) {
                System.out.print(game.whiteUsername());
            }
            else {
                System.out.print("None");
            }
            System.out.print("\n   Black: ");
            if (game.blackUsername() != null) {
                System.out.print(game.blackUsername());
            }
            else {
                System.out.print("None");
            }
            System.out.println();
        }
    }
}
