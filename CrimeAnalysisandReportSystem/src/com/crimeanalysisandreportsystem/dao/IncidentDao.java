package com.crimeanalysisandreportsystem.dao;

import com.crimeanalysisandreportsystem.entity.Incident;
import com.crimeanalysisandreportsystem.entity.IncidentStatus;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.IncidentNumberNotFoundException;

import java.util.Date;
import java.util.List;

public interface IncidentDao {
    boolean createIncident(Incident incident) throws DataAccessException;
    Incident getIncidentById(int incidentId) throws IncidentNumberNotFoundException, DataAccessException;
    List<Incident> getIncidentsInDateRange(Date startDate, Date endDate) throws DataAccessException;
    List<Incident> searchIncidents(String incidentType) throws DataAccessException;
    boolean updateIncidentStatus(IncidentStatus status, int incidentId) throws DataAccessException;
    List<Incident> getAllIncidents() throws DataAccessException;
}