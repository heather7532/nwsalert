package org.alpenlogic.weather.nwsalert.service.exception;

public class WeatherAlertMergeException extends RuntimeException {

    public WeatherAlertMergeException(String alertId, Throwable cause) {
        super("Failed to merge WeatherAlert with ID: " + alertId, cause);
    }

}