package domain;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerMapper {
    /*public String getPlayers() {
        try (java.sql.Connection connection = DriverManager.getConnection(Connection.JDBC_URL, "ID399789_g53", "7AAu9bEFEzLCbKb")) {
            String test = "select * from Speler where spelerID = 1";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(test);
            rs.next();
            return rs.getString("naam");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
*/
    public Player findPlayer(String firstname) {
        try (java.sql.Connection connection = DriverManager.getConnection(Connection.JDBC_URL, "ID399789_g53", "7AAu9bEFEzLCbKb")) {
            //String query = "select * from Speler where voornaam = " + firstname;
            String query = String.format("select * from Speler where voornaam = '%s'", firstname);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            //TODO: geboortedatum doorgeven ipv dummy value '0'
            //return new Player(rs.getString(rs.getString("voornaam")), rs.getString("naam"), (short)0);
            System.out.print(rs.getString("voornaam"));
            return null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}