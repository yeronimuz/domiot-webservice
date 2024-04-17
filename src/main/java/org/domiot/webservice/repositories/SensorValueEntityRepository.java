package org.domiot.webservice.repositories;

import org.lankheet.domiot.entities.SensorEntity;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface SensorValueEntityRepository extends PagingAndSortingRepository<SensorValueEntity, Long> {

    Page<SensorValueEntity> findBySensorEntityAndTimeStampBetweenOrderByTimeStamp(SensorEntity sensorEntity, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<SensorValueEntity> findByTimeStampBetweenOrderByTimeStamp(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
