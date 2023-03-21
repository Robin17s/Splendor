package domain;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PlayerMapper {
    public String getPlayers() {
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
}