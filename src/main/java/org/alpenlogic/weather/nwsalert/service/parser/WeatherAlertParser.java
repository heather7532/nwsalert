package org.alpenlogic.weather.nwsalert.service.parser;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.alpenlogic.weather.nwsalert.service.model.AlertStatus;
import org.alpenlogic.weather.nwsalert.service.model.WeatherAlert;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Slf4j
@Service
public class WeatherAlertParser {

    public WeatherAlert parseAlert(JsonNode feature) {
        JsonNode props = feature.path("properties");

        WeatherAlert alert = new WeatherAlert();
        alert.setId(props.path("id").asText());
        alert.setApiUrl(props.path("@id").asText());
        alert.setType(feature.path("type").asText());
        alert.setEvent(props.path("event").asText(null));
        alert.setSeverity(props.path("severity").asText(null));
        alert.setStatus(props.path("status").asText(null));
        alert.setHeadline(props.path("headline").asText(null));

        alert.setOnset(parseInstant(props.path("onset").asText(null)).orElse(null));
        alert.setExpires(parseInstant(props.path("expires").asText(null)).orElse(null));
        alert.setEnds(parseInstant(props.path("ends").asText(null)).orElse(null));
        alert.setRawAlert(feature.toString());
        alert.setReceived(Instant.now());

        alert.setAlertStatus(AlertStatus.fromString(alert.getStatus())); // null-safe mapper

        return alert;
    }

    private Optional<Instant> parseInstant(String text) {
        try {
            return text != null ? Optional.of(OffsetDateTime.parse(text).toInstant()) : Optional.empty();
        } catch (DateTimeParseException e) {
            log.warn("Invalid timestamp: {}", text);
            return Optional.empty();
        }
    }
}