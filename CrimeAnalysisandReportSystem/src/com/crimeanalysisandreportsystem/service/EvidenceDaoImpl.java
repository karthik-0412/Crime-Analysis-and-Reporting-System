package com.crimeanalysisandreportsystem.service;

import com.crimeanalysisandreportsystem.dao.EvidenceDao;
import com.crimeanalysisandreportsystem.entity.Evidence;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.EvidenceNotFoundException;
import com.crimeanalysisandreportsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvidenceDaoImpl implements EvidenceDao {
    private Connection connection;

    public EvidenceDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean createEvidence(Evidence evidence) throws DataAccessException {
        String query = "INSERT INTO evidence (description, locationFound, incidentID) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, evidence.getDescription());
            stmt.setString(2, evidence.getLocationFound());
            stmt.setInt(3, evidence.getIncidentID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create evidence", e);
        }
    }

    @Override
    public Evidence getEvidenceById(int evidenceId) throws EvidenceNotFoundException, DataAccessException {
        String query = "SELECT * FROM evidence WHERE evidenceID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, evidenceId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Evidence(
                    rs.getInt("evidenceID"),
                    rs.getString("description"),
                    rs.getString("locationFound"),
                    rs.getInt("incidentID")
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve evidence by ID", e);
        }
        throw new EvidenceNotFoundException("Evidence not found with ID: " + evidenceId);
    }

    @Override
    public List<Evidence> getAllEvidence() throws DataAccessException {
        List<Evidence> evidences = new ArrayList<>();
        String query = "SELECT * FROM evidence";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                evidences.add(new Evidence(
                    rs.getInt("evidenceID"),
                    rs.getString("description"),
                    rs.getString("locationFound"),
                    rs.getInt("incidentID")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all evidence", e);
        }
        return evidences;
    }

    @Override
    public boolean updateEvidence(Evidence evidence) throws DataAccessException {
        String query = "UPDATE evidence SET description = ?, locationFound = ?, incidentID = ? WHERE evidenceID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, evidence.getDescription());
            stmt.setString(2, evidence.getLocationFound());
            stmt.setInt(3, evidence.getIncidentID());
            stmt.setInt(4, evidence.getEvidenceID());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update evidence", e);
        }
    }

    @Override
    public boolean deleteEvidence(int evidenceId) throws DataAccessException {
        String query = "DELETE FROM evidence WHERE evidenceID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, evidenceId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete evidence", e);
        }
    }
}