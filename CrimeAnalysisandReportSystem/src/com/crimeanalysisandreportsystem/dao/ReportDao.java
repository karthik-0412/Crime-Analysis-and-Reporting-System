package com.crimeanalysisandreportsystem.dao;

import com.crimeanalysisandreportsystem.entity.Report;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.ReportNotFoundException;

import java.util.List;

public interface ReportDao {
    boolean createReport(Report report) throws DataAccessException;
    Report getReportById(int reportId) throws ReportNotFoundException, DataAccessException;
    List<Report> getAllReports() throws DataAccessException;
    boolean updateReport(Report report) throws DataAccessException;
    boolean deleteReport(int reportId) throws DataAccessException;
}