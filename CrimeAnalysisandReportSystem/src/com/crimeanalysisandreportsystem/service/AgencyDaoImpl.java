package com.crimeanalysisandreportsystem.service;

import com.crimeanalysisandreportsystem.dao.AgencyDao;
import com.crimeanalysisandreportsystem.entity.Agency;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.AgencyNotFoundException;
import com.crimeanalysisandreportsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AgencyDaoImpl implements AgencyDao {
    private Connection connection;

    public AgencyDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean createAgency(Agency agency) throws DataAccessException {
        String query = "INSERT INTO agency (agencyName, jurisdiction, contactInfo) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, agency.getAgencyName());
            stmt.setString(2, agency.getJurisdiction());
            stmt.setString(3, agency.getContactInformation());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create agency", e);
        }
    }

    @Override
    public Agency getAgencyById(int agencyId) throws AgencyNotFoundException, DataAccessException {
        String query = "SELECT * FROM agency WHERE agencyID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, agencyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Agency(
                    rs.getInt("agencyID"),
                    rs.getString("agencyName"),
                    rs.getString("jurisdiction"),
                    rs.getString("contactInfo")
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve agency by ID", e);
        }
        throw new AgencyNotFoundException("Agency not found with ID: " + agencyId);
    }

    @Override
    public List<Agency> getAllAgencies() throws DataAccessException {
        List<Agency> agencies = new ArrayList<>();
        String query = "SELECT * FROM agency";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                agencies.add(new Agency(
                    rs.getInt("agencyID"),
                    rs.getString("agencyName"),
                    rs.getString("jurisdiction"),
                    rs.getString("contactInfo")
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all agencies", e);
        }
        return agencies;
    }

    @Override
    public boolean updateAgency(Agency agency) throws DataAccessException {
        String query = "UPDATE agency SET agencyName = ?, jurisdiction = ?, contactInfo = ? WHERE agencyID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, agency.getAgencyName());
            stmt.setString(2, agency.getJurisdiction());
            stmt.setString(3, agency.getContactInformation());
            stmt.setInt(4, agency.getAgencyID());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update agency", e);
        }
    }

    @Override
    public boolean deleteAgency(int agencyId) throws DataAccessException {
        String query = "DELETE FROM agency WHERE agencyID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, agencyId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete agency", e);
        }
    }
}