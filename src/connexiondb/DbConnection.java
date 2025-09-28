package connexiondb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConnection {

private static DbConnection instance;
private Connection connection ;

private static final String Db_Url = System.getenv("Db_Host");
private static final String Db_User = System.getenv("Db_User");
private static final String Db_Password = System.getenv("Db_Password");

private DbConnection() {
	try {
		Class.forName("org.postgresql.Driver");
		this.connection = DriverManager.getConnection(Db_Url,Db_User,Db_Password);
		
	}catch (SQLException | ClassNotFoundException e) {
		throw new RuntimeException("Failed to initialize DB connection",e);
	}
	
}


public static DbConnection getInstance() {
	if(instance== null) {
		synchronized (DbConnection.class) {
			if (instance == null) {
				instance= new DbConnection();
			}
		}
	}
	return instance;
}

public Connection getConnection() {
	return connection;
}
	
}
	

