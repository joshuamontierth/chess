package utilities;
import java.util.Collection;

import model.GameData;

public record ListGamesResult(Collection<GameData> games) {
}
