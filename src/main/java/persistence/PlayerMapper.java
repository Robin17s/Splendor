package persistence;

import domain.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fetches Player data from the database, and serializes it into {@link domain.Player} objects.
 */
public class PlayerMapper {
    private final List<Player> PLAYERS = new ArrayList<>();

    /**
     * Tries to find a player based on the given parameters. If no player is found <code>null</code> will be returned
     * @param name The name of the player
     * @param yearOfBirth The year of birth of the player
     * @return The found Player, or null
     */
    public Player findPlayer(String name, int yearOfBirth) {
        if (PLAYERS.isEmpty()) loadPlayers();
        return PLAYERS.stream().filter(player -> player.getName().equalsIgnoreCase(name) && player.getDateOfBirth() == yearOfBirth).findFirst().orElse(null);
    }

    /**
     * Loads all player data from the database, and caches it, for later use.
     */
    public void loadPlayers() {
        try (Connection connection = ConnectionFactory.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT Name, YearOfBirth FROM Speler"); ResultSet set = statement.executeQuery()) {
            while (set.next()) PLAYERS.add(new Player(set.getString("Name"), set.getInt("YearOfBirth")));
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}