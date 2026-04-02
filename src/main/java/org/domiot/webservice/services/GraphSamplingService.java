package org.domiot.webservice.services;

import lombok.extern.slf4j.Slf4j;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GraphSamplingService {
    private static final int MINUTES_PER_DAY = 24 * 60 * 60;
    private static final int VALUES_PER_GRAPH = 240;
    public static final int MINUTES_PER_VALUE = MINUTES_PER_DAY / VALUES_PER_GRAPH;
    public static final int MINUTES_BETWEEN_VALUES = 6;
    public static final int MAX_MS_DEVIATION = 100;

    public List<SensorValueEntity> sample(List<SensorValueEntity> sensorValueEntities) {
        LocalDateTime baseTimestamp = sensorValueEntities.get(0).getTimeStamp();
        List<SensorValueEntity> sampled = sensorValueEntities.stream()
                .filter(sensorValueEntity ->
                        ChronoUnit.MINUTES.between(baseTimestamp, sensorValueEntity.getTimeStamp()) % 6 == 0)
                .toList();
        return sampled;
    }
}
