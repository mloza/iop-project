package database;

import java.sql.SQLException;

public class Testing {

	public static void main(String[] args) throws SQLException {
		PersonDAO dao = new PersonDAO();
		Person person = dao.findPerson("Jan");
		System.out.println("Imię i nazwisko: " + person.getFirstname() + " " + person.getLastname());
		System.out.println("Wiek: " + person.getAge());
		System.out.println("Pochodzenie: " + person.getCountry());
	}

}
