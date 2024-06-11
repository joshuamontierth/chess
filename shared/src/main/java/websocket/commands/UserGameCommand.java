package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
        move = null;
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    private final String authToken;

    private ChessMove move;
    private int gameID;
    private int colorCode;

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
    public int getColorCode() {
        return colorCode;
    }
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
    public int getGameID() {
        return gameID;
    }

    public void setMove (ChessMove move) {
        this.move = move;
    }
    public ChessMove getMove () {
        return move;
    }

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
