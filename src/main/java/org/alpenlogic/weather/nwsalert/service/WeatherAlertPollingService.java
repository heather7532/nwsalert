package org.alpenlogic.weather.nwsalert.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.alpenlogic.weather.nwsalert.service.exception.WeatherAlertMergeException;
import org.alpenlogic.weather.nwsalert.service.model.WeatherAlert;
import org.alpenlogic.weather.nwsalert.service.parser.WeatherAlertParser;
import org.alpenlogic.weather.nwsalert.service.repository.WeatherAlertRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Service
public class WeatherAlertPollingService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherAlertPollingService.class);

    private final WeatherAlertRepository repository;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Setter
    private Instant lastPollTime;

    @Value("${weather.alert.poll.url:https://api.weather.gov/alerts}")
    private String baseUrl;

    private final WeatherAlertParser parser;

    public WeatherAlertPollingService(
            WeatherAlertRepository repository,
            ObjectMapper objectMapper,
            RestTemplate restTemplate,
            WeatherAlertParser parser
    ) {
        this.repository = repository;
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
        this.restTemplate = restTemplate != null ? restTemplate : new RestTemplate();
        this.parser = parser;
        this.lastPollTime = Instant.now().minusSeconds(301);
    }

    @Scheduled(fixedDelayString = "${weather.alert.poll.interval.ms:300000}") // 5 min default
    public void pollAlerts() {
        processAlerts();
    }


    public void processAlerts() {
        String startTime = DateTimeFormatter.ISO_INSTANT.format(lastPollTime);
        String url = String.format("%s?status=actual&message_type=alert&start=%s", baseUrl, startTime);

        try {
            String response = restTemplate.getForObject(url, String.class);
            var root = objectMapper.readTree(response);
            var features = root.get("features");

            if (features != null && features.isArray()) {
                for (var feature : features) {
                    var parsedAlert = parser.parseAlert(feature);
                    String alertId = parsedAlert.getId();

                    WeatherAlert mergedAlert = repository.findById(alertId)
                            .map(existing -> {
                                try {
                                    return objectMapper.updateValue(existing, parsedAlert);
                                } catch (JsonMappingException e) {
                                    throw new WeatherAlertMergeException("Failed to merge alert with ID: " + alertId, e);
                                }
                            })
                            .orElse(parsedAlert);

                    repository.save(mergedAlert);
                }
            }

            lastPollTime = Instant.now();
        } catch (Exception e) {
            logger.error("Error polling weather alerts", e);
        }
    }

}