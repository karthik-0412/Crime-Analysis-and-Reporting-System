package com.crimeanalysisandreportsystem.service;

import com.crimeanalysisandreportsystem.dao.VictimDao;
import com.crimeanalysisandreportsystem.entity.Gender;
import com.crimeanalysisandreportsystem.entity.Victim;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.VictimNotFoundException;
import com.crimeanalysisandreportsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VictimDaoImpl implements VictimDao {
    private Connection connection;

    public VictimDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean createVictim(Victim victim) throws DataAccessException {
        String query = "INSERT INTO victims (firstName, lastName, dateOfBirth, gender, address, phoneNumber) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, victim.getFirstName());
            stmt.setString(2, victim.getLastName());
            stmt.setDate(3, new java.sql.Date(victim.getDateOfBirth().getTime()));
            stmt.setString(4, victim.getGender().name().replace('_', ' ')); 
            stmt.setString(5, victim.getAddress());
            stmt.setString(6, victim.getPhoneNumber());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create victim", e);
        }
    }

    @Override
    public Victim getVictimById(int victimId) throws VictimNotFoundException, DataAccessException {
        String query = "SELECT * FROM victims WHERE victimID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, victimId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Victim(
                    rs.getInt("victimID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getDate("dateOfBirth"),
                    Gender.valueOf(rs.getString("gender").toUpperCase()),  
                    rs.getString("address"),                
                    rs.getString("phoneNumber")             
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve victim by ID", e);
        }
        throw new VictimNotFoundException("Victim not found with ID: " + victimId);
    }


    @Override
    public List<Victim> getAllVictims() throws DataAccessException {
        List<Victim> victims = new ArrayList<>();
        String query = "SELECT * FROM victims";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                victims.add(new Victim(
                    rs.getInt("victimID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getDate("dateOfBirth"),
                    Gender.valueOf(rs.getString("gender").toUpperCase()),   
                    rs.getString("address"),                  
                    rs.getString("phoneNumber")               
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all victims", e);
        }
        return victims;
    }


    @Override
    public boolean updateVictim(Victim victim) throws DataAccessException {
        String query = "UPDATE victims SET firstName = ?, lastName = ?, dateOfBirth = ?, gender = ?, address = ?, phoneNumber = ? WHERE victimID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, victim.getFirstName());
            stmt.setString(2, victim.getLastName());
            stmt.setDate(3, new java.sql.Date(victim.getDateOfBirth().getTime()));
            stmt.setString(4, victim.getGender().name().replace('_', ' ').toUpperCase()); 
            stmt.setString(5, victim.getAddress());           
            stmt.setString(6, victim.getPhoneNumber());       	
            stmt.setInt(7, victim.getVictimID());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update victim", e);
        }
    }


    @Override
    public boolean deleteVictim(int victimId) throws DataAccessException {
        String query = "DELETE FROM victims WHERE victimID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, victimId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete victim", e);
        }
    }
}