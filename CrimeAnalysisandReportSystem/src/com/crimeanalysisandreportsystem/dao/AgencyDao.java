package com.crimeanalysisandreportsystem.dao;

import com.crimeanalysisandreportsystem.entity.Agency;
import com.crimeanalysisandreportsystem.exception.AgencyNotFoundException;
import com.crimeanalysisandreportsystem.exception.DataAccessException;

import java.util.List;

public interface AgencyDao {
    boolean createAgency(Agency agency) throws DataAccessException;
    Agency getAgencyById(int agencyId) throws AgencyNotFoundException, DataAccessException;
    List<Agency> getAllAgencies() throws DataAccessException;
    boolean updateAgency(Agency agency) throws DataAccessException;
    boolean deleteAgency(int agencyId) throws DataAccessException;
}