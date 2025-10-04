package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import java.sql.SQLException;

import connexiondb.DbConnection;

public class DatabaseConfig {

	private static final Logger logger = Logger.getLogger(DbConnection.class.getName());

    private static DatabaseConfig instance;
    private Connection connection;

    private static final String DB_URL = System.getenv("Db_Host"); // URL complète
    private static final String DB_USER = System.getenv("Db_User");
    private static final String DB_PASSWORD = System.getenv("Db_Password");


    private DatabaseConfig() {
        try {
            Class.forName("org.postgresql.Driver");

            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            logger.info("✅ Connexion PostgreSQL établie avec succès !");
        } catch (SQLException | ClassNotFoundException e) {
            logger.severe("❌ Erreur lors de l'initialisation de la connexion DB : " + e.getMessage());
            throw new RuntimeException("Échec initialisation DB", e);
        }
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                logger.warning("⚠️ Connexion fermée, tentative de reconnexion...");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (SQLException e) {
            logger.severe("❌ Impossible de reconnecter à la DB : " + e.getMessage());
            throw new RuntimeException("Échec reconnexion DB", e);
        }
        return connection;
    }

}
