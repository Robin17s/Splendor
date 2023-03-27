package domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerMapper {
    public Player findPlayer(String name, Short yearOfBirth) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT Name, YearOfBirth FROM Speler WHERE Name = ? AND YearOfBirth = ?")) {
                statement.setString(1, name);
                statement.setShort(2, yearOfBirth);
                try (ResultSet set = statement.executeQuery()) {
                    if (!(set.next())) throw new IllegalStateException("No player found!"); // TODO Make more good
                    return new Player(set.getString("Name"), set.getInt("YearOfBirth"));
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null; // TODO Optional?
    }

    //geen zin om onderstaande code te fixen, onnodig anyways

    /*public boolean createPlayer(Player player){
        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO Speler (naam, voornaam, geboortedatum, emailadres) VALUES (?, ?, ?, null)")) {
                statement.setString(1, player.getLastname());
                statement.setString(2, player.getFirstname());
                statement.setShort(3, player.getDateOfBirth());
                return statement.executeUpdate() == 1;

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
    public boolean removePlayer(String firstname){
        try (Connection connection = ConnectionFactory.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT spelerID, voornaam, naam, geboortedatum FROM Speler WHERE voornaam = ?")) {
                statement.setString(1, firstname);

                try (ResultSet set = statement.executeQuery()) {
                    if (!(set.next())) throw new IllegalStateException("Expected Data to be present!"); // TODO Make more good
                    int id = set.getInt("spelerID");
                    try (PreparedStatement statement1 = connection.prepareStatement("DELETE FROM Speler WHERE spelerID = ?")){
                        statement1.setString(1, Integer.toString(id));
                        return statement1.executeUpdate() == 1;
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }*/
}