package com.crimeanalysisandreportsystem.dao;

import com.crimeanalysisandreportsystem.entity.Evidence;
import com.crimeanalysisandreportsystem.exception.DataAccessException;
import com.crimeanalysisandreportsystem.exception.EvidenceNotFoundException;

import java.util.List;

public interface EvidenceDao {
    boolean createEvidence(Evidence evidence) throws DataAccessException;
    Evidence getEvidenceById(int evidenceId) throws EvidenceNotFoundException, DataAccessException;
    List<Evidence> getAllEvidence() throws DataAccessException;
    boolean updateEvidence(Evidence evidence) throws DataAccessException;
    boolean deleteEvidence(int evidenceId) throws DataAccessException;
}