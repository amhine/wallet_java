package connexiondb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static DbConnection instance;
    private Connection connection;

    private static final String DB_URL = System.getenv("Db_Host"); 
    private static final String DB_USER = System.getenv("Db_User");
    private static final String DB_PASSWORD = System.getenv("Db_Password");

    private DbConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Connexion PostgreSQL établie !");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to initialize DB connection", e);
        }
    }

    public static DbConnection getInstance() {
        if (instance == null) {
            synchronized (DbConnection.class) {
                if (instance == null) {
                    instance = new DbConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER , DB_PASSWORD);
    }
}
