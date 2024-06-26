package org.domiot.webservice.resources;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.services.SensorValueService;
import org.lankheet.domiot.api.SensorValueApi;
import org.lankheet.domiot.model.SensorValueListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController()
public class SensorValueResource implements SensorValueApi {

    private final SensorValueService sensorValueService;

    public SensorValueResource(final SensorValueService sensorValueService) {
        this.sensorValueService = sensorValueService;
    }

    @Override
    public ResponseEntity<SensorValueListResponse> getSensorValues(List<Long> sensorIds, LocalDateTime start, LocalDateTime end, Integer samplingTime) {
        log.debug("Get sensor values for sensor id {}", sensorIds);

        SensorValueListResponse sensorValueListResponse = sensorValueService.getSensorValues(sensorIds, start, end, samplingTime);

        return new ResponseEntity<>(sensorValueListResponse, HttpStatus.OK);
    }
}
