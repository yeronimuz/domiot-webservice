package org.domiot.webservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GraphSamplingServiceTest {

    GraphSamplingService graphSamplingService;

    @BeforeEach
    public void setup() {
        graphSamplingService = new GraphSamplingService();
    }

    @Test
    public void shouldReturnSampledSensorValues() {
        // Prepare data
        List<SensorValueEntity> sensorValueEntities;
        sensorValueEntities = new ArrayList<>();
        LocalDateTime now = LocalDateTime.of(2024, Month.APRIL, 28, 21, 12, 0, 100_000_000);
        for(int i=0; i<10; i++) {
            // For simplicity, adding sensor values for every minute
            sensorValueEntities.add(0, new SensorValueEntity(1L, 1L, now.minusMinutes(i), i));
        }

        List<SensorValueEntity> sampledSensorValues = graphSamplingService.sample(sensorValueEntities);

        assertEquals(2, sampledSensorValues.size());
    }
}
