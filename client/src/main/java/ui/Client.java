package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.GameData;
import utilities.HTMLException;
import utilities.ServerMessageObserver;
import utilities.WebsocketConnector;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.ResignCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.lang.System.exit;

public class Client implements ServerMessageObserver {
    private boolean firstPreLogin = true;
    private boolean firstPostLogin = true;
    private String authToken = null;
    private ServerFacade server;
    private HashMap<Integer,Integer> gameMap = new HashMap<Integer,Integer>();
    private GameData gameData = null;
    private String team;
    private WebsocketConnector websocket;
    private Integer gameID;
    private boolean gameplayState = false;

    public void run(String hostname, int port) throws Exception {
        server = new ServerFacade(hostname, port);
        websocket = new WebsocketConnector(hostname, port, this);
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
    private void printGameplayOptions() {
        System.out.println("1. Redraw Chess Board");
        System.out.println("2. Make Move");
        System.out.println("3. Highlight Legal Moves");
        System.out.println("4. Resign");
        System.out.println("5. Leave");
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
    private void postLogin() throws IOException {
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
                    break;
                case 4:
                    joinGame(true);
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
    private void gamePlay() throws IOException {
        printGameplayOptions();
        Scanner scanner = new Scanner(System.in);
        try {
            int option = scanner.nextInt();
            switch (option) {
                case 1:
                    drawBoard();
                    break;
                case 2:
                    makeMove();
                    break;
                case 3:
                    highlightMoves();
                    break;
                case 4:
                    resign();
                    break;
                case 5:
                    leave();
                    break;
                case 6:
                    printGameplayOptions();
                    break;
                default:
                    System.out.println("Invalid option, please enter a number between 1 and 6");

            }
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid input, please enter a number between 1 and 6");
        }
    }

    private void drawBoard() {
        BoardDrawer boardDrawer = new BoardDrawer(gameData.game().getBoard().getBoard());
        boolean whiteOrientation = !team.equals("Black");
        boardDrawer.drawBoard(whiteOrientation);
    }
    private void makeMove() throws IOException {
        System.out.println("Enter your move in the following format: e7e8 queen, where queen is the promotion piece if applicable.");
        String regex = "([a-h][1-8]){2} (queen|knight|bishop|rook)?";
        Pattern pattern = Pattern.compile(regex);
        Scanner scanner = new Scanner(System.in);
        String moveInput = scanner.nextLine();
        if (!pattern.matcher(moveInput).matches()) {
            System.out.println("Invalid notation, please enter your move in the following format: e7e8 queen, where queen is the promotion piece if applicable.");
            return;
        }
        int firstCol = moveInput.charAt(0) - 'a' + 1;
        int firstRow = Character.getNumericValue(moveInput.charAt(1));
        int secondCol = moveInput.charAt(2) - 'a' + 1;
        int secondRow = Character.getNumericValue(moveInput.charAt(3));
        String promotionString = moveInput.substring(5);
        promotionString = promotionString.toUpperCase();
        ChessPiece.PieceType promotionPiece = ChessPiece.PieceType.valueOf(promotionString);
        ChessMove move = new ChessMove(new ChessPosition(firstRow, firstCol), new ChessPosition(secondRow, secondCol),promotionPiece);
        MakeMoveCommand makeMoveCommand = new MakeMoveCommand(authToken,gameID,move);
        Gson gson = new Gson();
        websocket.send(gson.toJson(makeMoveCommand));
    }
    private void leave() throws IOException {
        System.out.println("Please confirm you would like to leave y/n");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (!input.equals("y")) {
            System.out.println("Resuming the game");
            return;
        }
        LeaveCommand leaveCommand = new LeaveCommand(authToken,gameID);
        Gson gson = new Gson();
        websocket.send(gson.toJson(leaveCommand));
    }
    private void resign() throws IOException {
        System.out.println("Please confirm you would like to resign y/n");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (!input.equals("y")) {
            System.out.println("Resuming normal gameplay");
            return;
        }
        ResignCommand resignCommand = new ResignCommand(authToken,gameID);
        Gson gson = new Gson();
        websocket.send(gson.toJson(resignCommand));
    }
    private void highlightMoves() {

    }

    private Collection<GameData> listGames() {
        try {
            Collection<GameData> games = server.listGames(authToken);
            printGameList(games);
            return games;
        }
        catch (HTMLException e) {
            System.out.println("Bad request: " + e.getMessage());
            return null;
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
    private void joinGame(boolean observerMode) throws IOException {
        var games = listGames();
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
            gameID = gameSelect;
            if (!observerMode) {
                int colorSelect = 0;
                System.out.println("Select 1 for white or 2 for black:");
                try {
                    colorSelect = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input, Select 1 for white or 2 for black:");
                }
                if (colorSelect != 1 && colorSelect != 2) {
                    System.out.println("Invalid input, Select 1 for white or 2 for black:");
                }

                try {
                    server.joinGame(gameSelect, colorSelect, authToken);
                    team = (gameSelect == 1) ? "White" : "Black";
                } catch (HTMLException e) {
                    if (e.getErrorCode() == 403) {
                        System.out.println("Already taken, please try again");
                    } else {
                        System.out.println("Bad request: " + e.getMessage());
                    }
                }
            }
            if (games != null) {
                for (var game : games) {
                    if (game.gameID() == gameSelect) {
                        gameData = game;
                    }
                }

            }
            ConnectCommand connectCommand = new ConnectCommand(authToken,gameID);
            Gson gson = new Gson();
            websocket.send(gson.toJson(connectCommand));
            while(gameplayState) {
                gamePlay();
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
    if (games.isEmpty()) {
        System.out.println("No games found");
        }
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> processNotification((NotificationMessage) message);
            case ERROR -> processError((ErrorMessage) message);
            case LOAD_GAME -> processLoadGame((LoadGameMessage) message);
        }

    }

    private void processLoadGame(LoadGameMessage message) {
        gameData = message.getGame();
        drawBoard();
        if(message.getMessageBody()!= null) {
            System.out.println(message.getMessageBody());
        }
    }

    private void processError(ErrorMessage message) {
        System.out.println(message.getMessageBody());
    }

    private void processNotification(NotificationMessage message) {
        System.out.println(message.getMessageBody());
    }
}
