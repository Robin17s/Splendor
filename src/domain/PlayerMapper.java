package domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerMapper {
    public Player findPlayer(String firstname) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM Speler WHERE voornaam = ?")) {
                statement.setString(1, firstname);

                try (ResultSet set = statement.executeQuery()) {
                    if (!(set.next())) throw new IllegalStateException("Expected Data to be present!"); // TODO Make more good
                    return new Player(set.getString("voornaam"), set.getString("naam"), set.getShort("geboortedatum"));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null; // TODO Optional?
    }
}