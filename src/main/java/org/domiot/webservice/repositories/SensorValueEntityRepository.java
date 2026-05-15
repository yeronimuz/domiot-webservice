package org.domiot.webservice.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.domiot.entities.SensorValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorValueEntityRepository extends JpaRepository<SensorValueEntity, Long> {

    List<SensorValueEntity> findBySensorIdAndTimeStampBetweenOrderByTimeStamp(Long sensorId, LocalDateTime start, LocalDateTime end);

}
