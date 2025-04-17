package com.crimeanalysisandreportsystem.dao;

import com.crimeanalysisandreportsystem.entity.Victim;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.VictimNotFoundException;

import java.util.List;

public interface VictimDao {
    boolean createVictim(Victim victim) throws DataAccessException;
    Victim getVictimById(int victimId) throws VictimNotFoundException, DataAccessException;
    List<Victim> getAllVictims() throws DataAccessException;
    boolean updateVictim(Victim victim) throws DataAccessException;
    boolean deleteVictim(int victimId) throws DataAccessException;
}