package org.alpenlogic.weather.nwsalert.service.repository;

import org.alpenlogic.weather.nwsalert.service.model.WeatherAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherAlertRepository extends JpaRepository<WeatherAlert, String> {
    // You can define custom queries here if needed
}
