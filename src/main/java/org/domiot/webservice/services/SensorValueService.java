package org.domiot.webservice.services;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.repositories.SensorEntityRepository;
import org.domiot.webservice.repositories.SensorValueEntityRepository;
import org.lankheet.domiot.entities.SensorEntity;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.lankheet.domiot.mapper.SensorMapper;
import org.lankheet.domiot.mapper.SensorValueMapper;
import org.lankheet.domiot.model.GraphMetadata;
import org.lankheet.domiot.model.NgxChartsData;
import org.lankheet.domiot.model.NgxChartsDataSeriesInner;
import org.lankheet.domiot.model.SensorValueGraphResponse;
import org.lankheet.domiot.model.SensorValueListResponse;
import org.lankheet.domiot.model.SensorValueSeriesItem;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This service will serve sensor values. It has two
 */
@Slf4j
@Service
public class SensorValueService {
    private static final int SAMPLING_OFFSET = 200;
    private final SensorValueEntityRepository sensorValueEntityRepository;
    private final SensorEntityRepository sensorEntityRepository;
    private final SensorValueMapper sensorValueMapper;
    private final SensorMapper sensorMapper;

    public SensorValueService(SensorValueEntityRepository sensorValueEntityRepository, SensorEntityRepository sensorEntityRepository, SensorValueMapper sensorValueMapper, SensorMapper sensorMapper) {
        this.sensorValueEntityRepository = sensorValueEntityRepository;
        this.sensorEntityRepository = sensorEntityRepository;
        this.sensorValueMapper = sensorValueMapper;
        this.sensorMapper = sensorMapper;
    }

    public SensorValueListResponse getSensorValues(List<Long> sensorIds, @NotNull LocalDateTime startTime, @NotNull LocalDateTime endTime, Integer samplingFactor) {
        SensorValueListResponse sensorValueListResponse = new SensorValueListResponse();
        if (sensorIds == null || sensorIds.isEmpty()) {
            log.error("No sensorId given, expected at least one value");
        } else {
            List<SensorEntity> sensorEntities = sensorEntityRepository.findSensorEntitiesByIdIn(sensorIds);
            if (sensorEntities != null && !sensorEntities.isEmpty()) {
                log.info("Sensors found: {}", sensorEntities.size());
                sensorEntities.forEach(sensor -> {
                    SensorValueSeriesItem seriesItem = new SensorValueSeriesItem();
                    seriesItem.setSamplingFactor(samplingFactor);
                    seriesItem.setSensor(sensorMapper.map(sensor));
                    List<SensorValueEntity> sensorValues = sensorValueEntityRepository.findBySensorIdAndTimeStampBetweenOrderByTimeStamp(sensor.getId(), startTime, endTime);
                    seriesItem.setTotal(sensorValues.size());
                    sensorValues = sample(sensorValues, samplingFactor);
                    seriesItem.setMax(sensorValues.stream().max(Comparator.comparing(SensorValueEntity::getValue)).orElseThrow(NoSuchElementException::new).getValue());
                    seriesItem.setMin(sensorValues.stream().min(Comparator.comparing(SensorValueEntity::getValue)).orElseThrow(NoSuchElementException::new).getValue());
                    seriesItem.setValues(sensorValueMapper.map(sensorValues));
                    sensorValueListResponse.addSeriesItem(seriesItem);
                });
            }
        }
        return sensorValueListResponse;
    }

    public SensorValueGraphResponse getSensorValueGraphData(List<Long> sensorIds, LocalDateTime start, LocalDateTime end) {
        SensorValueGraphResponse sensorValueGraphResponse = new SensorValueGraphResponse();
        if (sensorIds == null || sensorIds.isEmpty()) {
            log.error("No sensorId given, expected at least one value");
        } else {
            List<SensorEntity> sensorEntities = sensorEntityRepository.findSensorEntitiesByIdIn(sensorIds);
            if (sensorEntities != null && !sensorEntities.isEmpty()) {
                log.info("Sensors found: {}", sensorEntities.size());
                sensorEntities.forEach(sensor -> {
                    if (sensor != null) {
                        NgxChartsData ngxChartsData = new NgxChartsData();
                        ngxChartsData.setName(sensor.getName());
                        List<SensorValueEntity> sensorValues = sensorValueEntityRepository.findBySensorIdAndTimeStampBetweenOrderByTimeStamp(sensor.getId(), start, end);
                        if (sensorValues != null && !sensorValues.isEmpty()) {

                            double min = sensorValues.stream().min(Comparator.comparing(SensorValueEntity::getValue)).orElseThrow(NoSuchElementException::new).getValue();
                            double max = sensorValues.stream().max(Comparator.comparing(SensorValueEntity::getValue)).orElseThrow(NoSuchElementException::new).getValue();
                            sensorValueGraphResponse.putMetaDataItem(sensor.getName(), new GraphMetadata().max(max).min(min).total(sensorValues.size()));
                            ngxChartsData.setSeries(graphify(sensorValues));
                            sensorValueGraphResponse.addValuesItem(ngxChartsData);
                        }
                    }
                });
            }
        }
        return sensorValueGraphResponse;
    }

    private List<NgxChartsDataSeriesInner> graphify(List<SensorValueEntity> values) {
        return values.stream()
                .map(sensorValueEntity -> {
                    NgxChartsDataSeriesInner ngxChartsDataSeriesInner = new NgxChartsDataSeriesInner();
                    ngxChartsDataSeriesInner.setName(sensorValueEntity.getTimeStamp().toString());
                    ngxChartsDataSeriesInner.setValue(sensorValueEntity.getValue());
                    return ngxChartsDataSeriesInner;
                })
                .collect(Collectors.toList());
    }

    private List<SensorValueEntity> sample(List<SensorValueEntity> sensorValueEntities, Integer samplingFactor) {
        if (sensorValueEntities.size() <= SAMPLING_OFFSET) {
            return sensorValueEntities;
        }
        return IntStream.range(0, sensorValueEntities.size())
                .filter(i -> i % samplingFactor == 0)
                .mapToObj(sensorValueEntities::get)
                .collect(Collectors.toList());
    }
}
