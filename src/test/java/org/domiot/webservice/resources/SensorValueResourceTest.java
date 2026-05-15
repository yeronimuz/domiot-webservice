package org.domiot.webservice.resources;

import org.domiot.webservice.services.SensorValueService;
import org.domiot.model.SensorValueListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SensorValueResourceTest {

    @Mock
    private SensorValueService sensorValueService;

    @InjectMocks
    private SensorValueResource sensorValueResource;

    @Test
    void getSensorValuesShouldReturnOkWithServiceResult() {
        List<Long> sensorIds = List.of(1L, 2L);
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now();
        Integer sampling = 5;
        SensorValueListResponse expected = new SensorValueListResponse();

        when(sensorValueService.getSensorValues(sensorIds, start, end, sampling)).thenReturn(expected);

        ResponseEntity<SensorValueListResponse> response =
                sensorValueResource.getSensorValues(sensorIds, start, end, sampling);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expected, response.getBody());
    }
}

