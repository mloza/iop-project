package database;

public class Person {
	private int id;
	private String firstname;
	private String lastname;
	private int age;
	private String country;
	
	public Person(String firstname, String lastname, int age, String country) {
		setFirstname(firstname);
		setLastname(lastname);
		setAge(age);
		setCountry(country);
	}
	
	public int getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
