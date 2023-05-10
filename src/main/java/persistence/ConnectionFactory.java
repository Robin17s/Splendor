package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates Database Connections.
 */
public final class ConnectionFactory {
    private static final String JDBC_URL = "jdbc:mysql://ID399789_g53.db.webhosting.be/ID399789_g53";

    /**
     * Instantiates a new ConnectionFactory.
     * <p>
     * ConnectionFactory is a utility class, and should <b>never</b> be instantiated.
     * <p>
     * Creating an instance of this class will do nothing.
     */
    private ConnectionFactory() {}

    /**
     * Creates a Database connection, and returns it.
     * @return A connection to the Splendor G53 database.
     * @throws SQLException When something goes wrong whilst connecting, logging in, or maintaining the Database connection.
     */
    public static Connection getConnection() throws SQLException { return DriverManager.getConnection(JDBC_URL, "ID399789_g53", "7AAu9bEFEzLCbKb"); }
}