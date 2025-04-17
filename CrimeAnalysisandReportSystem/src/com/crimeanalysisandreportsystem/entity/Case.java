package com.crimeanalysisandreportsystem.entity;

import java.util.List;

public class Case {

	private int caseId;
	private String caseDescription;
	private String caseStatus;
	private int assignedOfficerId;
	private List<Incident> incidents;
	
	public Case() {
		super();
	}

	public Case(String caseDescription, String caseStatus, int assignedOfficerId, List<Incident> incidents) {
		super();
		this.caseDescription = caseDescription;
		this.caseStatus = caseStatus;
		this.assignedOfficerId = assignedOfficerId;
		this.incidents = incidents;
	}

	public Case(int caseId, String caseDescription, String caseStatus, int assignedOfficerId,
			List<Incident> incidents) {
		super();
		this.caseId = caseId;
		this.caseDescription = caseDescription;
		this.caseStatus = caseStatus;
		this.assignedOfficerId = assignedOfficerId;
		this.incidents = incidents;
	}

	public int getCaseId() {
		return caseId;
	}

	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	public String getCaseDescription() {
		return caseDescription;
	}

	public void setCaseDescription(String caseDescription) {
		this.caseDescription = caseDescription;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public int getAssignedOfficerId() {
		return assignedOfficerId;
	}

	public void setAssignedOfficerId(int assignedOfficerId) {
		this.assignedOfficerId = assignedOfficerId;
	}

	public List<Incident> getIncidents() {
		return incidents;
	}

	public void setIncidents(List<Incident> incidents) {
		this.incidents = incidents;
	}

	@Override
	public String toString() {
		return "Case [caseId=" + caseId + ", caseDescription=" + caseDescription + ", caseStatus=" + caseStatus
				+ ", assignedOfficerId=" + assignedOfficerId + ", incidents=" + incidents + "]";
	}

	
}
