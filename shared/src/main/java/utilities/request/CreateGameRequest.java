package utilities.request;

public record CreateGameRequest(String authToken, String gameName) {
}
