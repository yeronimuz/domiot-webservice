package org.domiot.webservice.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.services.SensorValueService;
import org.lankheet.domiot.api.SensorValueApi;
import org.lankheet.domiot.model.SensorValue;
import org.lankheet.domiot.model.SensorValueListResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
public class SensorValueResource implements SensorValueApi {

    private final SensorValueService sensorValueService;

    public SensorValueResource(final SensorValueService sensorValueService) {
        this.sensorValueService = sensorValueService;
    }

    @GetMapping("/sensor-values")
    @Override
    public SensorValueListResponse getSensorValues(@NotNull Integer sensorId, @NotNull LocalDateTime start, @NotNull LocalDateTime end, @Min(0L) Integer offset, @Min(0L) @Max(200L) Integer limit) {
        log.debug("Get sensor values for sensor id {}", sensorId);

        SensorValueListResponse sensorValueListResponse = new SensorValueListResponse();
        List<SensorValue> sensorValues = sensorValueService.getSensorValues((long) sensorId, start, end, offset, limit);
        sensorValueListResponse.setResult(sensorValues);
        sensorValueListResponse.setTotal((long) sensorValues.size());
        sensorValueListResponse.setOffset(offset);
        sensorValueListResponse.setLimit(limit);

        return sensorValueListResponse;
    }

}
