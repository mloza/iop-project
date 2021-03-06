package common;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

import javax.swing.*;

public class Person {
	private int id;
	private String firstname;
	private String lastname;
	private double matchCoefficient = -9999.99;
	private IplImage picture;

	public JFrame getFrameWindow()
	{
		return frameWindow;
	}

	public void setFrameWindow(JFrame frameWindow)
	{
		this.frameWindow = frameWindow;
	}

	private JFrame frameWindow;
    private Integer window;

    public Integer getWindow() {
        return window;
    }

    public void setWindow(Integer window) {
        this.window = window;
    }



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

	public IplImage getPicture() {
		return picture;
	}

	public void setPicture(IplImage picture) {
		this.picture = picture;
	}
}
