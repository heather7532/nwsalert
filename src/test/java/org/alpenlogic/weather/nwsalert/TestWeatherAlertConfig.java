package org.alpenlogic.weather.nwsalert;

import org.alpenlogic.weather.nwsalert.service.repository.WeatherAlertRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Profile("test")
public class TestWeatherAlertConfig {

    @Bean
    @Primary  // ✅ Ensures this is used instead of the real one
    public WeatherAlertRepository mockWeatherAlertRepository() {
        return mock(WeatherAlertRepository.class);
    }
}