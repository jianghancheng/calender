package calender;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HerokuDatabase {
	public static Connection getConnection() throws URISyntaxException,
	SQLException {


String username ="rgknkgncxwomjd";
String password ="aGoPdgHJYDCta_hct9k6tYsJwL";
String dbUrl = "jdbc:postgresql://ec2-54-197-241-24.compute-1.amazonaws.com:5432/dfa6mgqqm3s8kk?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

return DriverManager.getConnection(dbUrl, username, password);
}
}
