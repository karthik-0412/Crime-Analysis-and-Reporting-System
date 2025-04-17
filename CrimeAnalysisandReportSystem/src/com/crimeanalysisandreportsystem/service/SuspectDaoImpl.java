package com.crimeanalysisandreportsystem.service;

import com.crimeanalysisandreportsystem.entity.Suspect;
import com.crimeanalysisandreportsystem.dao.SuspectDao;
import com.crimeanalysisandreportsystem.entity.Gender;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.SuspectNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SuspectDaoImpl implements SuspectDao {

    private final Connection connection;

    public SuspectDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean createSuspect(Suspect suspect) throws DataAccessException {
        String sql = "INSERT INTO Suspects (FirstName, LastName, DateOfBirth, Gender, Address, PhoneNumber) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, suspect.getFirstName());
            stmt.setString(2, suspect.getLastName());
            stmt.setDate(3, new java.sql.Date(suspect.getDateOfBirth().getTime()));
            stmt.setString(4, suspect.getGender().name().replace('_', ' ')); // Enum to DB String
            stmt.setString(5, suspect.getAddress());
            stmt.setString(6, suspect.getPhoneNumber());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error creating suspect", e);
        }
    }

    @Override
    public Suspect getSuspectById(int suspectId) throws SuspectNotFoundException, DataAccessException {
        String sql = "SELECT * FROM Suspects WHERE SuspectID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, suspectId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToSuspect(rs);
                } else {
                    throw new SuspectNotFoundException("Suspect with ID " + suspectId + " not found.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving suspect", e);
        }
    }

    @Override
    public List<Suspect> getAllSuspects() throws DataAccessException {
        List<Suspect> suspects = new ArrayList<>();
        String sql = "SELECT * FROM Suspects";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                suspects.add(mapRowToSuspect(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error retrieving all suspects", e);
        }
        return suspects;
    }

    @Override
    public boolean updateSuspect(Suspect suspect) throws DataAccessException {
        String sql = "UPDATE Suspects SET FirstName=?, LastName=?, DateOfBirth=?, Gender=?, Address=?, PhoneNumber=? WHERE SuspectID=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, suspect.getFirstName());
            stmt.setString(2, suspect.getLastName());
            stmt.setDate(3, new java.sql.Date(suspect.getDateOfBirth().getTime()));
            stmt.setString(4, suspect.getGender().name().replace('_', ' ')); // Enum to DB String
            stmt.setString(5, suspect.getAddress());
            stmt.setString(6, suspect.getPhoneNumber());
            stmt.setInt(7, suspect.getSuspectID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error updating suspect", e);
        }
    }

    @Override
    public boolean deleteSuspect(int suspectId) throws DataAccessException {
        String sql = "DELETE FROM Suspects WHERE SuspectID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, suspectId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting suspect", e);
        }
    }

    // Private utility method to map ResultSet row to Suspect object
    private Suspect mapRowToSuspect(ResultSet rs) throws SQLException {
        int suspectID = rs.getInt("SuspectID");
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        Date dateOfBirth = rs.getDate("DateOfBirth");
        Gender gender = Gender.valueOf(rs.getString("Gender").toUpperCase().replace(" ", "_")); // DB String to Enum
        String address = rs.getString("Address");
        String phoneNumber = rs.getString("PhoneNumber");

        return new Suspect(suspectID, firstName, lastName, dateOfBirth, gender, address, phoneNumber);
    }
}
