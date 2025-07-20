package org.alpenlogic.weather.nwsalert;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AlertSseStreamTest {

    @Test
    void testAlertStreamReceivesEvents() throws Exception {
        URL url = new URL("http://localhost:8080/alerts/stream");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "text/event-stream");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {

            String line;
            int eventCount = 0;
            Instant endTime = Instant.now().plusSeconds(10); // read for 10 seconds

            while (Instant.now().isBefore(endTime) && (line = reader.readLine()) != null) {
                if (line.startsWith("data:")) {
                    eventCount++;
                    System.out.println("Received SSE event: " + line);
                }
            }

            System.out.println("Total alerts received: " + eventCount);
        }
    }
}