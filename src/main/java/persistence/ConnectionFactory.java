package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionFactory {
    private static final String JDBC_URL = "jdbc:mysql://ID399789_g53.db.webhosting.be/ID399789_g53";

    private ConnectionFactory() {}

    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(JDBC_URL, "ID399789_g53", "7AAu9bEFEzLCbKb"); }
}