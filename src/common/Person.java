package common;

public class Person {
	private int id;
	private String firstname;
	private String lastname;
	private double matchCoefficient;
	
	public Person() {
		
	}
	
	public Person(String firstname, String lastname, double matchCoefficient) {
		setFirstname(firstname);
		setLastname(lastname);
		setMatchCoefficient(matchCoefficient);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
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

	public double getMatchCoefficient() {
		return matchCoefficient;
	}

	public void setMatchCoefficient(double matchCoefficient) {
		this.matchCoefficient= matchCoefficient;
	}
}
