package com.crimeanalysisandreportsystem.dao;

import com.crimeanalysisandreportsystem.entity.Officer;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.OfficerNotFoundException;

import java.util.List;

public interface OfficerDao {
    boolean createOfficer(Officer officer) throws DataAccessException;
    Officer getOfficerById(int officerId) throws OfficerNotFoundException, DataAccessException;
    List<Officer> getAllOfficers() throws DataAccessException;
    boolean updateOfficer(Officer officer) throws DataAccessException;
    boolean deleteOfficer(int officerId) throws DataAccessException;
    
}