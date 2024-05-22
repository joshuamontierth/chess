package utilities;

public record JoinGameRequest(String AuthToken, String playerColor, int gameID) {
}
