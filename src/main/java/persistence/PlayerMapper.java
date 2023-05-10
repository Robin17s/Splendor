package persistence;

import domain.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Fetches Player data from the database, and serializes it into {@link domain.Player} objects.
 */
public class PlayerMapper {
    /**
     * Tries to find a player based on the given parameters. If no player is found <code>null</code> will be returned
     * @param name The name of the player
     * @param yearOfBirth The year of birth of the player
     * @return The found Player, or null
     */
    public Player findPlayer(String name, int yearOfBirth) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT Name, YearOfBirth FROM Speler WHERE Name = ? AND YearOfBirth = ?")) {
                statement.setString(1, name);
                statement.setInt(2, yearOfBirth);
                try (ResultSet set = statement.executeQuery()) {
                    //if (!(set.next())) throw new IllegalStateException("No player found!"); // TODO Make more good
                    if (!(set.next())) return null; // Temp fix for FX
                    return new Player(set.getString("Name"), set.getInt("YearOfBirth"));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null; // TODO Optional?
    }
}