package com.crimeanalysisandreportsystem.dao;

import com.crimeanalysisandreportsystem.entity.Suspect;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.SuspectNotFoundException;

import java.util.List;

public interface SuspectDao {
    boolean createSuspect(Suspect suspect) throws DataAccessException;
    Suspect getSuspectById(int suspectId) throws SuspectNotFoundException, DataAccessException;
    List<Suspect> getAllSuspects() throws DataAccessException;
    boolean updateSuspect(Suspect suspect) throws DataAccessException;
    boolean deleteSuspect(int suspectId) throws DataAccessException;
}