package org.domiot.webservice.repositories;

import org.lankheet.domiot.entities.DeviceEntity;
import org.lankheet.domiot.entities.SensorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link DeviceEntity}
 */
@Repository
public interface SensorEntityRepository extends JpaRepository<SensorEntity, Long> {
    SensorEntity findDistinctByNameAndSensorTypeValue(String name, int type);
}
