package org.domiot.webservice.repositories;

import org.domiot.entities.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for {@link DeviceEntity}
 */
@Repository
public interface DeviceEntityRepository extends JpaRepository<DeviceEntity, Long> {
    DeviceEntity findByMacAddress(String macAddress);

    boolean existsByMacAddress(String macAddress);

    List<DeviceEntity> findBySiteEntityId(Long siteId);

    Optional<DeviceEntity> findByIdAndSiteEntityId(Long deviceId, Long siteId);
}
