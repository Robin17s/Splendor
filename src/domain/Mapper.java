package domain;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Mapper {
	public void getPlayers() {
		try (java.sql.Connection connection = DriverManager.getConnection(Connection.JDBC_URL, "ID399789_g53", "7AAu9bEFEzLCbKb")) {

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
}