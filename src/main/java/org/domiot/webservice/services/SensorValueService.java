package org.domiot.webservice.services;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.repositories.SensorEntityRepository;
import org.domiot.webservice.repositories.SensorValueEntityRepository;
import org.lankheet.domiot.entities.SensorEntity;
import org.lankheet.domiot.mapper.SensorValueMapper;
import org.lankheet.domiot.model.SensorValue;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class SensorValueService {
    private final SensorValueEntityRepository sensorValueEntityRepository;
    private final SensorEntityRepository sensorEntityRepository;
    private final SensorValueMapper sensorValueMapper;

    public SensorValueService(SensorValueEntityRepository sensorValueEntityRepository, SensorEntityRepository sensorEntityRepository, SensorValueMapper sensorValueMapper) {
        this.sensorValueEntityRepository = sensorValueEntityRepository;
        this.sensorEntityRepository = sensorEntityRepository;
        this.sensorValueMapper = sensorValueMapper;
    }

    public List<SensorValue> getSensorValues(@NotNull Long sensorId, @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime, @Min(0L) Integer offset, @Min(0L) @Max(200L) Integer limit) {
        SensorEntity sensorEntity = sensorEntityRepository.findById(sensorId).orElse(null);
        if (sensorEntity == null) {
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(offset, limit);
        return sensorValueMapper.map(sensorValueEntityRepository.findBySensorEntityAndTimeStampBetween(
                sensorEntity,
                startTime,
                endTime,
                pageable));
    }
}
