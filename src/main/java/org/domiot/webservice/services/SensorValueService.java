package org.domiot.webservice.services;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.repositories.SensorEntityRepository;
import org.domiot.webservice.repositories.SensorValueEntityRepository;
import org.lankheet.domiot.entities.SensorEntity;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.lankheet.domiot.mapper.SensorValueMapper;
import org.lankheet.domiot.model.SensorValueListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public SensorValueListResponse getSensorValues(Integer sensorId, @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime, @Min(0L) Integer offset, @Min(0L) @Max(200L) Integer limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<SensorValueEntity> returnPage = null;
        SensorValueListResponse sensorValueListResponse = new SensorValueListResponse();
        if (sensorId == null) {
            // Get all sensor values in the timeframe
            returnPage = sensorValueEntityRepository.findByTimeStampBetweenOrderByTimeStamp(startTime, endTime, Pageable.unpaged());
        } else {
            SensorEntity sensorEntity = sensorEntityRepository.findById(sensorId.longValue()).orElse(null);
            if (sensorEntity != null) {
                log.info("Sensor id {} found",  sensorId);
                returnPage = sensorValueEntityRepository.findBySensorEntityAndTimeStampBetweenOrderByTimeStamp(sensorEntity, startTime, endTime, pageable);
            }
        }
        if (returnPage != null) {
            sensorValueListResponse.setResult(sensorValueMapper.map(returnPage.getContent()));
            sensorValueListResponse.setPageNumber(returnPage.getNumber());
            sensorValueListResponse.setPageSize(returnPage.getSize());
            sensorValueListResponse.setTotal(returnPage.getTotalElements());
        }
        return sensorValueListResponse;
    }
}
