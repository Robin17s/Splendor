package domain;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Mapper {
	public void getPlayers() {
		try (java.sql.Connection connection = DriverManager.getConnection(Connection.JDBC_URL)) {

		} catch (SQLException exception) {
			exception.printStackTrace();
		}
	}
}