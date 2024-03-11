package org.domiot.webservice.repositories;

import org.lankheet.domiot.entities.DeviceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for {@link DeviceEntity}
 */
@Repository
public interface DeviceEntityRepository extends CrudRepository<DeviceEntity, Long> {
    DeviceEntity findByMacAddress(String macAddress);
}
