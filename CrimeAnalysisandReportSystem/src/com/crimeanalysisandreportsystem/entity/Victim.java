package com.crimeanalysisandreportsystem.entity;

import java.util.Date;

public class Victim {
	

	private int victimID;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
    private String address;
    private String phoneNumber;
	
    
    
    public Victim(String firstName, String lastName, Date dateOfBirth, Gender gender, String address,
			String phoneNumber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
    
    public Victim(int victimID, String firstName, String lastName, Date dateOfBirth, Gender gender, String address,
			String phoneNumber) {
		super();
		this.victimID = victimID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.gender = gender;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public int getVictimID() {
		return victimID;
	}

	public void setVictimID(int victimID) {
		this.victimID = victimID;
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
		return "Victim [victimID=" + victimID + ", firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
				+ dateOfBirth + ", gender=" + gender + ", address=" + address + ", phoneNumber=" + phoneNumber + "]";
	}

	
	
}
