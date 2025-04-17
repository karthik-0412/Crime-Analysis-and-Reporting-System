package com.crimeanalysisandreportsystem.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Incident {

	
	private int incidentID;
	private String incidentType;
	private Date incidentDate;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private String description;
	private IncidentStatus status;
	private Integer victimID;
	private Integer suspectID;
	private Integer officerID;

	// Getters and Setters
	public int getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(int incidentID) {
		this.incidentID = incidentID;
	}

	public String getIncidentType() {
		return incidentType;
	}

	public void setIncidentType(String incidentType) {
		this.incidentType = incidentType;
	}

	public Date getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(Date incidentDate) {
		this.incidentDate = incidentDate;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public IncidentStatus getStatus() {
		return status;
	}

	public void setStatus(IncidentStatus status) {
		this.status = status;
	}
	
	public Integer getVictimID() {
		return victimID;
	}

	public void setVictimID(Integer victimID) {
		this.victimID = victimID;
	}

	public Integer getSuspectID() {
		return suspectID;
	}

	public void setSuspectID(Integer suspectID) {
		this.suspectID = suspectID;
	}

	public Integer getOfficerID() {
		return officerID;
	}

	public void setOfficerID(Integer officerID) {
		this.officerID = officerID;
	}


	

	public Incident(String incidentType, Date incidentDate, BigDecimal bigDecimal, BigDecimal bigDecimal2,
			String description, IncidentStatus string, Integer victimID, Integer suspectID, Integer officerID) {
		this.incidentType = incidentType;
		this.incidentDate = incidentDate;
		this.latitude = bigDecimal;
		this.longitude = bigDecimal2;
		this.description = description;
		this.status = string;
		this.victimID = victimID;
		this.suspectID = suspectID;
		this.officerID = officerID;
	}
	
	

	public Incident(int incidentID, String incidentType, Date incidentDate, BigDecimal latitude, BigDecimal longitude,
			String description, IncidentStatus status, Integer victimID, Integer suspectID, Integer officerID) {
		super();
		this.incidentID = incidentID;
		this.incidentType = incidentType;
		this.incidentDate = incidentDate;
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
		this.status = status;
		this.victimID = victimID;
		this.suspectID = suspectID;
		this.officerID = officerID;
	}

	@Override
	public String toString() {
		return "Incident [incidentID=" + incidentID + ", incidentType=" + incidentType + ", incidentDate="
				+ incidentDate + ", latitude=" + latitude + ", longitude=" + longitude + ", description=" + description
				+ ", status=" + status + ", victimID=" + victimID + ", suspectID=" + suspectID + ", officerID="
				+ officerID + "]";
	}
}
