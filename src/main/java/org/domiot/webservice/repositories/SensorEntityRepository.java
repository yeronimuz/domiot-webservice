package org.domiot.webservice.repositories;

import org.lankheet.domiot.entities.DeviceEntity;
import org.lankheet.domiot.entities.SensorEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link DeviceEntity}
 */
@Repository
public interface SensorEntityRepository extends CrudRepository<SensorEntity, Long> {
    SensorEntity findDistinctByNameAndSensorTypeValue(String name, int type);
}
