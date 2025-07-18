package org.alpenlogic.weather.nwsalert;

import org.alpenlogic.weather.nwsalert.service.model.WeatherAlert;
import org.alpenlogic.weather.nwsalert.service.WeatherAlertPollingService;
import org.alpenlogic.weather.nwsalert.service.repository.WeatherAlertRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestWeatherAlertConfig.class)
public class WeatherAlertPollingServiceTest {

    @Autowired
    private WeatherAlertPollingService service;

    @Autowired
    private WeatherAlertRepository repository;

    @Test
    void testProcessAlertsAgainstLiveEndpoint() {
        // Setup
        service.setLastPollTime(Instant.now().minusSeconds(3600));

        // Act
        service.processAlerts();

        // Capture all saved alerts
        ArgumentCaptor<WeatherAlert> captor = ArgumentCaptor.forClass(WeatherAlert.class);
        verify(repository, atLeastOnce()).save(captor.capture());

        List<WeatherAlert> savedAlerts = captor.getAllValues();

        // Assert
        assertThat(savedAlerts).isNotEmpty();

        // Print saved alert summaries
        System.out.println("Saved alerts:");
        for (WeatherAlert alert : savedAlerts) {
            System.out.printf(" - ID: %s, Event: %s%n", alert.getId(), alert.getEvent());
        }
    }
}