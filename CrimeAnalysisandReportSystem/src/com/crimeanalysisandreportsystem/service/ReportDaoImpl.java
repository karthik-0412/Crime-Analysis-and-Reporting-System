package com.crimeanalysisandreportsystem.service;

import com.crimeanalysisandreportsystem.dao.ReportDao;
import com.crimeanalysisandreportsystem.entity.Report;
import com.crimeanalysisandreportsystem.entity.ReportStatus;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.ReportNotFoundException;
import com.crimeanalysisandreportsystem.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportDaoImpl implements ReportDao {
    private Connection connection;

    public ReportDaoImpl(Connection connection) {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public boolean createReport(Report report) throws DataAccessException {
        String query = "INSERT INTO reports (incidentID, OfficerID, reportDate, reportDetails, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, report.getIncidentID());  
            stmt.setInt(2, report.getReportingOfficer());   
            stmt.setDate(3, new java.sql.Date(report.getReportDate().getTime())); 
            stmt.setString(4, report.getReportDetails());  
            stmt.setString(5, report.getStatus().toString());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to create report", e);
        }
    }


    @Override
    public Report getReportById(int reportId) throws ReportNotFoundException, DataAccessException {
        String query = "SELECT * FROM reports WHERE reportID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reportId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Report(
                    rs.getInt("reportID"),
                    rs.getInt("incidentID"),
                    rs.getInt("OfficerID"),
                    rs.getDate("reportDate"),
                    rs.getString("reportDetails"),
                    ReportStatus.valueOf(rs.getString("status").toUpperCase())
                );
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve report by ID", e);
        }
        throw new ReportNotFoundException("Report not found with ID: " + reportId);
    }

    @Override
    public List<Report> getAllReports() throws DataAccessException {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT * FROM reports";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reports.add(new Report(
                    rs.getInt("reportID"),
                    rs.getInt("incidentID"),
                    rs.getInt("OfficerID"),
                    rs.getDate("reportDate"),
                    rs.getString("reportDetails"),
                    ReportStatus.valueOf(rs.getString("status").toUpperCase())
                ));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all reports", e);
        }
        return reports;
    }

    @Override
    public boolean updateReport(Report report) throws DataAccessException {
        String query = "UPDATE reports SET incidentID = ?, OfficerId = ?, reportDate = ?, reportDetails = ?, status = ? WHERE reportID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, report.getIncidentID());
            stmt.setInt(2, report.getReportingOfficer());
            stmt.setDate(3, new java.sql.Date(report.getReportDate().getTime()));
            stmt.setString(4, report.getReportDetails());
            stmt.setString(5, report.getStatus().name().replace('_', ' ').toUpperCase());
            stmt.setInt(6, report.getReportID());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to update report", e);
        }
    }

    @Override
    public boolean deleteReport(int reportId) throws DataAccessException {
        String query = "DELETE FROM reports WHERE reportID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reportId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete report", e);
        }
    }
}