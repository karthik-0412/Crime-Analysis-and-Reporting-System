package com.crimeanalysisandreportsystem.entity;

public enum ReportStatus {
    DRAFT,
    FINALIZED;

    public static ReportStatus fromString(String value) {
        return ReportStatus.valueOf(value.toUpperCase());
    }
}