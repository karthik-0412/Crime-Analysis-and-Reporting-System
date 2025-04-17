package com.crimeanalysisandreportsystem.exception;

@SuppressWarnings("serial")
public class AgencyNotFoundException extends Exception {
    public AgencyNotFoundException(String message) {
        super(message);
    }
}