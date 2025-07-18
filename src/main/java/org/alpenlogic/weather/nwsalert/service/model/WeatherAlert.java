package org.alpenlogic.weather.nwsalert.service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "weather_alert")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String apiUrl; // maps to properties.@id
    private String type;
    private String event;
    private String severity;
    private String status;
    private Instant onset;
    private Instant expires;
    private Instant ends;
    private String headline;

    @Enumerated(EnumType.STRING)
    private AlertStatus alertStatus;

    @Column(name = "raw_alert", columnDefinition = "jsonb")
    private String rawAlert;

    private Instant received;
}