package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PersonDAO {
	static final String url = "jdbc:h2:~/inteligenteye";
	
	static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, "pk", "pk");
	}
	
	public int insertPerson() {
		return 0;
		
	}
	
	public boolean deletePerson() {
		return false;
		
	}
	
	// TODO Na razie zakładam, że tylko jedna osoba może spełnić podane kryteria
	public Person findPerson(String name) throws SQLException {
		Person person = null;
		
		Connection conn = getConnection();
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM person WHERE firstname = ?");
		stmt.setString(1, name);
		ResultSet rs = stmt.executeQuery();
		
		person = new Person();
		while(rs.next()) {
			person.setId(rs.getInt("personId"));
			person.setFirstname(rs.getString("firstname"));
			person.setLastname(rs.getString("lastname"));
			person.setAge(rs.getInt("age"));
			person.setCountry(rs.getString("country"));
		}
		
		return person;
		
	}
	
	public boolean updatePerson() {
		return false;
		
	}
}
