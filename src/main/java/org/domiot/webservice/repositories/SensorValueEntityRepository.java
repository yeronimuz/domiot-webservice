package org.domiot.webservice.repositories;

import org.lankheet.domiot.entities.SensorEntity;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorValueEntityRepository extends CrudRepository<SensorValueEntity, Long> {
    List<SensorValueEntity> findBySensorEntityAndTimeStampBetween(SensorEntity sensorEntity, LocalDateTime start, LocalDateTime end, Pageable pageable);
}
