package org.alpenlogic.weather.nwsalert.service.repository;

import org.alpenlogic.weather.nwsalert.service.model.WeatherAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherAlertRepository extends JpaRepository<WeatherAlert, String> {
    // You can define custom queries here if needed
    @Query("SELECT wa FROM WeatherAlert wa WHERE wa.status = 'ACTIVE' AND wa.expires > CURRENT_TIMESTAMP")
    List<WeatherAlert> findActiveAlerts();
}