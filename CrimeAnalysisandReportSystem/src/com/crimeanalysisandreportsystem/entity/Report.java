package com.crimeanalysisandreportsystem.entity;

import java.util.Date;

public class Report {
	
	private int reportID;
    private int incidentID;
    private int reportingOfficer;
    private Date reportDate;
    private String reportDetails;
    private ReportStatus status;
	
    public Report(int incidentID, int reportingOfficer, Date reportDate, String reportDetails,
			ReportStatus status) {
		super();
		this.incidentID = incidentID;
		this.reportingOfficer = reportingOfficer;
		this.reportDate = reportDate;
		this.reportDetails = reportDetails;
		this.status = status;
	}
    
    
    
    
    public Report(int reportID, int incidentID, int reportingOfficer, Date reportDate, String reportDetails,
			ReportStatus status) {
		super();
		this.reportID = reportID;
		this.incidentID = incidentID;
		this.reportingOfficer = reportingOfficer;
		this.reportDate = reportDate;
		this.reportDetails = reportDetails;
		this.status = status;
	}




	public int getReportID() {
		return reportID;
	}

	public void setReportID(int reportID) {
		this.reportID = reportID;
	}

	public int getIncidentID() {
		return incidentID;
	}

	public void setIncidentID(int incidentID) {
		this.incidentID = incidentID;
	}

	public int getReportingOfficer() {
		return reportingOfficer;
	}

	public void setReportingOfficer(int reportingOfficer) {
		this.reportingOfficer = reportingOfficer;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getReportDetails() {
		return reportDetails;
	}

	public void setReportDetails(String reportDetails) {
		this.reportDetails = reportDetails;
	}

	public ReportStatus getStatus() {
		return status;
	}

	public void setStatus(ReportStatus status) {
		this.status = status;
	}
    
	@Override
	public String toString() {
		return "Report [reportID=" + reportID + ", incidentID=" + incidentID + ", reportingOfficer=" + reportingOfficer
				+ ", reportDate=" + reportDate + ", reportDetails=" + reportDetails + ", status=" + status + "]";
	}


}
