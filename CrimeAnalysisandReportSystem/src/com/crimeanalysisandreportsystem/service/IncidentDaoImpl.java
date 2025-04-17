package com.crimeanalysisandreportsystem.service;

import com.crimeanalysisandreportsystem.dao.IncidentDao;
import com.crimeanalysisandreportsystem.entity.Incident;
import com.crimeanalysisandreportsystem.entity.IncidentStatus;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.IncidentNumberNotFoundException;
import com.crimeanalysisandreportsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IncidentDaoImpl implements IncidentDao {
    private Connection connection;

    public IncidentDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean createIncident(Incident incident) throws DataAccessException {
        boolean success = false;
        if (incident == null) {
            throw new DataAccessException("Incident cannot be null");
        }
        try {
            String sql = "INSERT INTO Incidents (IncidentType, IncidentDate, Latitude, Longitude, Description, Status, VictimID, SuspectID, OfficerId) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, incident.getIncidentType());
                ps.setTimestamp(2, new java.sql.Timestamp(incident.getIncidentDate().getTime()));
                ps.setBigDecimal(3, incident.getLatitude());
                ps.setBigDecimal(4, incident.getLongitude());
                ps.setString(5, incident.getDescription());
                ps.setString(6, incident.getStatus().name());
                ps.setInt(7, incident.getVictimID());
                ps.setInt(8, incident.getSuspectID());
                ps.setInt(9, incident.getOfficerID());
                
                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            incident.setIncidentID(rs.getInt(1));  
                        }
                    }
                    success = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error while creating incident", e);
        }
        return success;
    }

    @Override
    public Incident getIncidentById(int incidentId) throws IncidentNumberNotFoundException, DataAccessException {
        String query = "SELECT * FROM Incidents WHERE IncidentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, incidentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToIncident(rs);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve incident by ID", e);
        }
        throw new IncidentNumberNotFoundException("Incident not found with ID: " + incidentId);
    }

    @Override
    public List<Incident> getIncidentsInDateRange(Date startDate, Date endDate) throws DataAccessException {
        List<Incident> incidents = new ArrayList<>();
        String query = "SELECT * FROM Incidents WHERE IncidentDate BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setTimestamp(1, new java.sql.Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new java.sql.Timestamp(endDate.getTime()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                incidents.add(mapResultSetToIncident(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve incidents in date range", e);
        }
        return incidents;
    }

    @Override
    public List<Incident> searchIncidents(String incidentType) throws DataAccessException {
        List<Incident> incidents = new ArrayList<>();
        String query = "SELECT * FROM Incidents WHERE LOWER(IncidentType) = LOWER(?)\r\n";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, incidentType);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                incidents.add(mapResultSetToIncident(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to search incidents by type", e);
        }
        return incidents;
    }

    @Override
    public boolean updateIncidentStatus(IncidentStatus status, int incidentId) throws DataAccessException {
        String query = "UPDATE Incidents SET Status = ? WHERE IncidentID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
        	 stmt.setString(1, status.name().replace('_', ' '));;
            stmt.setInt(2, incidentId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update incident status", e);
        }
    }

    private Incident mapResultSetToIncident(ResultSet rs) throws SQLException {
        return new Incident(
                rs.getInt("IncidentID"),
                rs.getString("IncidentType"),
                rs.getTimestamp("IncidentDate"),
                rs.getBigDecimal("Latitude"),
                rs.getBigDecimal("Longitude"),
                rs.getString("Description"),
                IncidentStatus.valueOf(rs.getString("Status").replace(' ', '_').toUpperCase()),
                rs.getObject("VictimID") != null ? rs.getInt("VictimID") : null,
                rs.getObject("SuspectID") != null ? rs.getInt("SuspectID") : null,
                rs.getObject("OfficerID") != null ? rs.getInt("OfficerID") : null
        );
    }
    
    @Override
    public List<Incident> getAllIncidents() throws DataAccessException {
        List<Incident> incidents = new ArrayList<>();
        String query = "SELECT * FROM Incidents";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                incidents.add(mapResultSetToIncident(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all incidents", e);
        }
        return incidents;
    }

}
