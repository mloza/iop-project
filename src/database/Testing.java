package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Testing {

	public static void main(String[] args) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:h2:~/test");
		Statement st = conn.createStatement();
		conn.close();
	}

}
