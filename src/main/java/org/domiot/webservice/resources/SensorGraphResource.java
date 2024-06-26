package org.domiot.webservice.resources;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.services.SensorValueService;
import org.lankheet.domiot.api.SensorGraphDataApi;
import org.lankheet.domiot.model.SensorValueGraphResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController()
public class SensorGraphResource implements SensorGraphDataApi {

    private final SensorValueService sensorValueService;

    public SensorGraphResource(final SensorValueService sensorValueService) {
        this.sensorValueService = sensorValueService;
    }

    @Override
    public ResponseEntity<SensorValueGraphResponse> getSensorValueGraphData(List<Long> sensorIds, LocalDateTime start, LocalDateTime end) {
        return ResponseEntity.ok().body(sensorValueService.getSensorValueGraphData(sensorIds, start, end));
    }

}
