package org.alpenlogic.weather.nwsalert.service.model;

public enum AlertStatus {
    ACTUAL,
    EXERCISE,
    SYSTEM,
    TEST,
    UNKNOWN;  // fallback for unrecognized values

    public static AlertStatus fromString(String status) {
        if (status == null) return UNKNOWN;
        return switch(status.toUpperCase()) {
            case "ACTUAL" -> ACTUAL;
            case "EXERCISE" -> EXERCISE;
            case "SYSTEM" -> SYSTEM;
            case "TEST" -> TEST;
            default -> UNKNOWN;
        };
    }
}