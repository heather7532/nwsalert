package org.alpenlogic.weather.nwsalert.service.sse;

import org.alpenlogic.weather.nwsalert.service.model.WeatherAlert;
import org.alpenlogic.weather.nwsalert.service.repository.WeatherAlertRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@RestController
public class AlertSseController {

    private final AlertEventBroadcaster broadcaster;
    private final WeatherAlertRepository repository;

    public AlertSseController(AlertEventBroadcaster broadcaster, WeatherAlertRepository repository) {
        this.broadcaster = broadcaster;
        this.repository = repository;
    }

    @GetMapping("/alerts/stream")
    public SseEmitter streamAlerts() throws IOException {
        SseEmitter emitter = broadcaster.registerClient();

        // Send all active alerts as initial events
        List<WeatherAlert> activeAlerts = repository.findActiveAlerts();
        for (WeatherAlert alert : activeAlerts) {
            try {
                emitter.send(SseEmitter.event()
                        .name("alert")
                        .data(alert));
            } catch (IOException e) {
                emitter.completeWithError(e);
                return emitter;
            }
        }

        return emitter;
    }
}