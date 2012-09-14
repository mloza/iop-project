package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Testing {

	public static void main(String[] args) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:h2:~/inteligenteye", "pk", "pk");
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM person;");
		while(rs.next()) {
			System.out.println(rs.getInt("personId") + " " +
							   rs.getString("firstname") + " " +
							   rs.getString("lastname") + " " + 
							   rs.getInt("age") + " " +
							   rs.getString("country") + " "
					);
		}
		st.close();
		conn.close();
	}

}
