package com.crimeanalysisandreportsystem.exception;

@SuppressWarnings("serial")
public class IncidentNumberNotFoundException extends Exception {
    public IncidentNumberNotFoundException(String message) {
        super(message);
    }
}