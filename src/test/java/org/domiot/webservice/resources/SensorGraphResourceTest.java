package org.domiot.webservice.resources;

import org.domiot.webservice.services.SensorValueService;
import org.domiot.model.SensorValueGraphResponse;
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
class SensorGraphResourceTest {

    @Mock
    private SensorValueService sensorValueService;

    @InjectMocks
    private SensorGraphResource sensorGraphResource;

    @Test
    void getSensorValueGraphDataShouldReturnOkWithServiceResult() {
        List<Long> sensorIds = List.of(3L, 4L);
        LocalDateTime start = LocalDateTime.now().minusHours(2);
        LocalDateTime end = LocalDateTime.now();
        SensorValueGraphResponse expected = new SensorValueGraphResponse();

        when(sensorValueService.getSensorValueGraphData(sensorIds, start, end)).thenReturn(expected);

        ResponseEntity<SensorValueGraphResponse> response =
                sensorGraphResource.getSensorValueGraphData(sensorIds, start, end);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(expected, response.getBody());
    }
}

