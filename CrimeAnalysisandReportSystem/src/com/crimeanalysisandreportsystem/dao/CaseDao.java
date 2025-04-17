package com.crimeanalysisandreportsystem.dao;

import com.crimeanalysisandreportsystem.entity.Case;
import com.crimeanalysisandreportsystem.entity.Incident;
import com.crimeanalysisandreportsystem.exception.CaseNotFoundException;
import com.crimeanalysisandreportsystem.exception.DataAccessException;

import java.util.List;

public interface CaseDao {
    Case createCase(String caseDescription, String caseStatus, int assignedOfficerId, List<Incident> incidents) throws DataAccessException;
    Case getCaseDetails(int caseId) throws CaseNotFoundException, DataAccessException;
    boolean updateCaseDetails(Case aCase) throws DataAccessException;
    List<Case> getAllCases() throws DataAccessException;
    List<Case> getCasesByTopOfficer() throws DataAccessException;
}