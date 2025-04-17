package com.crimeanalysisandreportsystem.service;

import com.crimeanalysisandreportsystem.dao.OfficerDao;
import com.crimeanalysisandreportsystem.entity.Officer;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.OfficerNotFoundException;
import com.crimeanalysisandreportsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OfficerDaoImpl implements OfficerDao {
    private Connection connection;

    public OfficerDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
	    public boolean createOfficer(Officer officer) throws DataAccessException {
	        String query = "INSERT INTO officers (firstName, lastName, badgeNumber, `Rank`, contactInfo, agencyID) VALUES ( ?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement stmt = connection.prepareStatement(query)) {
	            stmt.setString(1, officer.getFirstName());
	            stmt.setString(2, officer.getLastName());
	            stmt.setString(3, officer.getBadgeNumber());
	            stmt.setString(4, officer.getRank());
	            stmt.setString(5, officer.getContactInformation());
	            stmt.setInt(6, officer.getAgencyID());
	            stmt.executeUpdate();
	            return true;
	        } catch (SQLException e) {
	            throw new DataAccessException("Failed to create officer", e);
	        }
	    }

    @Override
    public Officer getOfficerById(int officerId) throws OfficerNotFoundException, DataAccessException {
        String query = "SELECT * FROM officers WHERE officerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, officerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Officer(
                    rs.getInt("officerID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("badgeNumber"),
                    rs.getString("rank"),
                    rs.getString("contactInfo"),
                    rs.getInt("agencyID")
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve officer by ID", e);
        }
        throw new OfficerNotFoundException("Officer not found with ID: " + officerId);
    }

    @Override
    public List<Officer> getAllOfficers() throws DataAccessException {
        List<Officer> officers = new ArrayList<>();
        String query = "SELECT * FROM officers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                officers.add(new Officer(
                    rs.getInt("officerID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("badgeNumber"),
                    rs.getString("rank"),
                    rs.getString("contactInfo"),
                    rs.getInt("agencyID")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all officers", e);
        }
        return officers;
    }

    @Override
    public boolean updateOfficer(Officer officer) throws DataAccessException {
        String query = "UPDATE officers SET firstName = ?, lastName = ?, badgeNumber = ?, `rank` = ?, contactInfo = ?, agencyID = ? WHERE officerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, officer.getFirstName());
            stmt.setString(2, officer.getLastName());
            stmt.setString(3, officer.getBadgeNumber());
            stmt.setString(4, officer.getRank());
            stmt.setString(5, officer.getContactInformation());
            stmt.setInt(6, officer.getAgencyID());
            stmt.setInt(7, officer.getOfficerID());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update officer", e);
        }
    }

    @Override
    public boolean deleteOfficer(int officerId) throws DataAccessException {
        String query = "DELETE FROM officers WHERE officerID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, officerId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete officer", e);
        }
    }

}