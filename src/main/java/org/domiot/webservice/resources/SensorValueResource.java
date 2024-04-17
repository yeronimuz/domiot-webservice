package org.domiot.webservice.resources;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.services.SensorValueService;
import org.lankheet.domiot.api.SensorValueApi;
import org.lankheet.domiot.model.SensorValueListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController()
public class SensorValueResource implements SensorValueApi {

    private final SensorValueService sensorValueService;

    public SensorValueResource(final SensorValueService sensorValueService) {
        this.sensorValueService = sensorValueService;
    }

    @Override
    public ResponseEntity<SensorValueListResponse> getSensorValues(LocalDateTime start, Integer pageNumber, Integer pageSize, Integer sensorId, LocalDateTime end) {
        log.debug("Get sensor values for sensor id {}", sensorId);

        SensorValueListResponse sensorValueListResponse = sensorValueService.getSensorValues(sensorId, start, end, pageNumber, pageSize);

        return new ResponseEntity<>(sensorValueListResponse, HttpStatus.OK);
    }
}
