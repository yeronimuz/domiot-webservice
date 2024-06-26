package org.domiot.webservice.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorValueCachingServiceTest {
    private static final LocalDateTime NOW =
            LocalDateTime.of(2024, Month.APRIL, 28, 21, 16, 44);
    private static final LocalDateTime THEN = LocalDateTime.of(2024, Month.APRIL, 28, 23, 16, 44);
    private static final SensorValueEntity SENSOR_VALUE_ENTITY_1 = new SensorValueEntity();
    private static final SensorValueEntity SENSOR_VALUE_ENTITY_2 = new SensorValueEntity();
    private static final SensorValueEntity SENSOR_VALUE_ENTITY_3 = new SensorValueEntity();
    private static final SensorValueEntity SENSOR_VALUE_ENTITY_4 = new SensorValueEntity();
    SensorValueCachingService sensorValueCachingService = new SensorValueCachingService();

    @BeforeAll
    static void doSetup() {
        SENSOR_VALUE_ENTITY_1.setSensorId(1L);
        SENSOR_VALUE_ENTITY_1.setTimeStamp(NOW);
        SENSOR_VALUE_ENTITY_1.setValue(1.0);

        SENSOR_VALUE_ENTITY_2.setSensorId(1L);
        SENSOR_VALUE_ENTITY_2.setTimeStamp(NOW.plusMinutes(10));
        SENSOR_VALUE_ENTITY_2.setValue(2.0);

        SENSOR_VALUE_ENTITY_3.setSensorId(1L);
        SENSOR_VALUE_ENTITY_3.setTimeStamp(NOW.plusMinutes(30));
        SENSOR_VALUE_ENTITY_3.setValue(2.0);

        SENSOR_VALUE_ENTITY_4.setSensorId(1L);
        SENSOR_VALUE_ENTITY_4.setTimeStamp(THEN);
        SENSOR_VALUE_ENTITY_4.setValue(4.0);
    }

    @BeforeEach
    void setup() {
        sensorValueCachingService = new SensorValueCachingService();
    }

    @Test
    void testGetCachedValues() {
        SensorValueRequest sensorValueRequest = new SensorValueRequest();
        sensorValueCachingService.addValues(sensorValueRequest, Arrays.asList(SENSOR_VALUE_ENTITY_1, SENSOR_VALUE_ENTITY_2));

        List<SensorValueEntity> result = sensorValueCachingService.getCachedValues(sensorValueRequest);

        assertThat(result.get(0), is(SENSOR_VALUE_ENTITY_1));
        assertThat(result.get(1), is(SENSOR_VALUE_ENTITY_2));
    }

    @Test
    void testCacheContainsSuperSet() {
        LocalDateTime now = LocalDateTime.now();
        SensorValueRequest cachedValueRequest = new SensorValueRequest(1, NOW, THEN, now);
        SensorValueRequest sensorValueRequest = new SensorValueRequest(1, SENSOR_VALUE_ENTITY_2.getTimeStamp(), SENSOR_VALUE_ENTITY_3.getTimeStamp(), now);

        sensorValueCachingService.addValues(cachedValueRequest, Arrays.asList(SENSOR_VALUE_ENTITY_1, SENSOR_VALUE_ENTITY_2, SENSOR_VALUE_ENTITY_3, SENSOR_VALUE_ENTITY_4));

        List<SensorValueEntity> result = sensorValueCachingService.getCachedValues(sensorValueRequest);

        assertThat(result.size(), is(2));
        assertThat(result.get(0), is(SENSOR_VALUE_ENTITY_2));
        assertThat(result.get(1), is(SENSOR_VALUE_ENTITY_3));
    }
}
