package com.crimeanalysisandreportsystem.entity;

import java.util.Date;

public class Suspect {
   
	private int suspectID;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
    private String address;
    private String phoneNumber;

    public Suspect(String firstName, String lastName, Date dateOfBirth, Gender gender, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    
    

    public Suspect(int suspectID, String firstName, String lastName, Date dateOfBirth, Gender gender, String address,
			String phoneNumber) {
		super();
		this.suspectID = suspectID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}



	// Getters & Setters

    public int getSuspectID() {
        return suspectID;
    }

    public void setSuspectID(int suspectID) {
        this.suspectID = suspectID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    @Override
   	public String toString() {
   		return "Suspect [suspectID=" + suspectID + ", firstName=" + firstName + ", lastName=" + lastName
   				+ ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", address=" + address + ", phoneNumber="
   				+ phoneNumber + "]";
   	}
    
}
