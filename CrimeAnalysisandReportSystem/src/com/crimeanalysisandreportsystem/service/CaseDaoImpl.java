package com.crimeanalysisandreportsystem.service;


import com.crimeanalysisandreportsystem.dao.CaseDao;
import com.crimeanalysisandreportsystem.entity.Case;
import com.crimeanalysisandreportsystem.entity.Incident;
import com.crimeanalysisandreportsystem.entity.IncidentStatus;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.CaseNotFoundException;
import com.crimeanalysisandreportsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CaseDaoImpl implements CaseDao {
    private Connection connection;

    public CaseDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Case createCase(String caseDescription, String caseStatus, int assignedOfficerId, List<Incident> incidents) throws DataAccessException {
        String query = "INSERT INTO cases (caseDescription, caseStatus, assignedOfficerId) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, caseDescription);
            stmt.setString(2, caseStatus);
            stmt.setInt(3, assignedOfficerId);
            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int caseId = generatedKeys.getInt(1);
                Case newCase = new Case(caseId, caseDescription, caseStatus, assignedOfficerId, incidents);
                String insertIncidentQuery = "INSERT INTO case_incidents (caseID, incidentID) VALUES (?, ?)";
                try (PreparedStatement incidentStmt = connection.prepareStatement(insertIncidentQuery)) {
                    for (Incident incident : incidents) {
                        incidentStmt.setInt(1, caseId);
                        incidentStmt.setInt(2, incident.getIncidentID());
                        incidentStmt.executeUpdate();
                    }
                }
                return newCase;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create case", e);
        }
        return null;
    }

    @Override
    public Case getCaseDetails(int caseId) throws CaseNotFoundException, DataAccessException {
        String query = "SELECT * FROM cases WHERE caseId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, caseId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String caseDescription = rs.getString("caseDescription");
                String caseStatus = rs.getString("caseStatus");
                int assignedOfficerId = rs.getInt("assignedOfficerId");
                List<Incident> incidents = getIncidentsForCase(caseId);
                return new Case(caseId, caseDescription, caseStatus, assignedOfficerId, incidents);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve case details", e);
        }
        throw new CaseNotFoundException("Case not found with ID: " + caseId);
    }

    private List<Incident> getIncidentsForCase(int caseId) throws DataAccessException {
        List<Incident> incidents = new ArrayList<>();
        String query = "SELECT i.* FROM incidents i JOIN case_incidents ci ON i.incidentID = ci.incidentID WHERE ci.caseID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, caseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Incident incident = new Incident(
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
                incidents.add(incident);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve incidents for case", e);
        }
        return incidents;
    }

    @Override
    public boolean updateCaseDetails(Case aCase) throws DataAccessException {
        String query = "UPDATE cases SET caseDescription = ?, caseStatus = ?, assignedOfficerId = ? WHERE caseId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, aCase.getCaseDescription());
            stmt.setString(2, aCase.getCaseStatus());
            stmt.setInt(3, aCase.getAssignedOfficerId());
            stmt.setInt(4, aCase.getCaseId());
            stmt.executeUpdate();
            String deleteIncidentsQuery = "DELETE FROM case_incidents WHERE caseID = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteIncidentsQuery)) {
                deleteStmt.setInt(1, aCase.getCaseId());
                deleteStmt.executeUpdate();
            }
            String insertIncidentQuery = "INSERT INTO case_incidents (caseID, incidentID) VALUES (?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertIncidentQuery)) {
                for (Incident incident : aCase.getIncidents()) {
                    insertStmt.setInt(1, aCase.getCaseId());
                    insertStmt.setInt(2, incident.getIncidentID());
                    insertStmt.executeUpdate();
                }
            }
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update case details", e);
        }
    }

    @Override
    public List<Case> getAllCases() throws DataAccessException {
        List<Case> cases = new ArrayList<>();
        String query = "SELECT * FROM cases";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int caseId = rs.getInt("caseId");
                String caseDescription = rs.getString("caseDescription");
                String caseStatus = rs.getString("caseStatus");
                int assignedOfficerId = rs.getInt("assignedOfficerId");
                List<Incident> incidents = getIncidentsForCase(caseId);
                cases.add(new Case(caseId, caseDescription, caseStatus, assignedOfficerId, incidents));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all cases", e);
        }
        return cases;
    }
    
    @Override
    public List<Case> getCasesByTopOfficer() throws DataAccessException {
        List<Case> cases = new ArrayList<>();

        String query = """
            SELECT * FROM cases
            WHERE assignedOfficerID IN (
                SELECT assignedOfficerID
                FROM cases
                GROUP BY assignedOfficerID
                HAVING COUNT(*) = (
                    SELECT MAX(case_count) FROM (
                        SELECT COUNT(*) AS case_count
                        FROM cases
                        GROUP BY assignedOfficerID
                    ) AS counts
                )
            )
            """;

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Case c = new Case();
                c.setCaseId(rs.getInt("caseID"));
                c.setCaseDescription(rs.getString("caseDescription"));
                c.setCaseStatus(rs.getString("caseStatus"));
                c.setAssignedOfficerId(rs.getInt("assignedOfficerID"));                
                cases.add(c);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve cases by top officer", e);
        }

        return cases;
    }

}


