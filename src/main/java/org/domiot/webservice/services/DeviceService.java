package org.domiot.webservice.services;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.repositories.DeviceEntityRepository;
import org.domiot.webservice.repositories.SiteRespository;
import org.lankheet.domiot.entities.DeviceEntity;
import org.lankheet.domiot.entities.SiteEntity;
import org.lankheet.domiot.mapper.DeviceMapper;
import org.lankheet.domiot.model.Device;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DeviceService {
    private final DeviceEntityRepository deviceEntityRepository;
    private final SiteRespository siteRepository;
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceEntityRepository deviceEntityRepository, SiteRespository siteRepository, DeviceMapper deviceMapper) {
        this.deviceEntityRepository = deviceEntityRepository;
        this.siteRepository = siteRepository;
        this.deviceMapper = deviceMapper;
    }

    public List<Device> addDevices(Long siteId, List<Device> deviceList) {
        if (siteId == null) {
            throw new IllegalArgumentException("siteId cannot be null");
        }
        if (deviceList == null || deviceList.isEmpty()) {
            return Collections.emptyList();
        }

        SiteEntity site = siteRepository.findById(siteId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown siteId: " + siteId));

        List<DeviceEntity> mappedEntities = deviceMapper.mapToEntities(deviceList);
        mappedEntities.forEach(deviceEntity -> deviceEntity.setSiteEntity(site));

        List<DeviceEntity> deviceEntities = deviceEntityRepository.saveAll(mappedEntities);
        return deviceMapper.mapToDto(deviceEntities);
    }

    public List<Device> getDevice(Long siteId, Long deviceId) {
        Optional<DeviceEntity> deviceEntity = deviceEntityRepository.findByIdAndSiteEntityId(deviceId, siteId);
        return deviceEntity.map(entity -> List.of(deviceMapper.map(entity))).orElse(Collections.emptyList());
    }

    public List<Device> getSiteDevices(Long siteId) {
        return deviceMapper.mapToDto(deviceEntityRepository.findBySiteEntityId(siteId));
    }

    public List<Device> updateDevice(Long siteId, Long deviceId, Device device) {
        if (device == null) {
            throw new IllegalArgumentException("device cannot be null");
        }
        DeviceEntity existing = deviceEntityRepository.findByIdAndSiteEntityId(deviceId, siteId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown deviceId " + deviceId + " for siteId " + siteId));

        DeviceEntity updated = deviceMapper.map(device);
        updated.setId(existing.getId());
        updated.setSiteEntity(existing.getSiteEntity());

        DeviceEntity savedEntity = deviceEntityRepository.save(updated);
        return List.of(deviceMapper.map(savedEntity));
    }
}
