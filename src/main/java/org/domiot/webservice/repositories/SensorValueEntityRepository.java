package org.domiot.webservice.repositories;

import org.lankheet.domiot.entities.SensorEntity;
import org.lankheet.domiot.entities.SensorValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorValueEntityRepository extends JpaRepository<SensorValueEntity, Long> {

    List<SensorValueEntity> findBySensorIdAndTimeStampBetweenOrderByTimeStamp(Long sensorId, LocalDateTime start, LocalDateTime end);

}
