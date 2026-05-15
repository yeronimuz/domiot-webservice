package org.domiot.webservice.resources;

import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.domiot.webservice.services.SensorValueService;
import org.domiot.api.SensorValueApi;
import org.domiot.model.SensorValueListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
public class SensorValueResource implements SensorValueApi {

    private final SensorValueService sensorValueService;

    public SensorValueResource(final SensorValueService sensorValueService) {
        this.sensorValueService = sensorValueService;
    }

    @Override
    public ResponseEntity<SensorValueListResponse> getSensorValues(List<Long> sensorIds, LocalDateTime start, LocalDateTime end, Integer samplingFactor) {
        log.debug("Get sensor values for sensor id {}", sensorIds);

        SensorValueListResponse sensorValueListResponse = sensorValueService.getSensorValues(sensorIds, start, end, samplingFactor);

        return ResponseEntity.ok(sensorValueListResponse);
    }
}
