package org.domiot.webservice.services;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.repositories.DeviceEntityRepository;
import org.lankheet.domiot.entities.DeviceEntity;
import org.lankheet.domiot.mapper.DeviceMapper;
import org.lankheet.domiot.model.Device;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DeviceService {
    private final DeviceEntityRepository deviceEntityRepository;
    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceEntityRepository deviceEntityRepository, DeviceMapper deviceMapper) {
        this.deviceEntityRepository = deviceEntityRepository;
        this.deviceMapper = deviceMapper;
    }

    public List<Device> addDevices(Long siteId, List<Device> deviceList) {
        List<Device> returnDeviceList = new ArrayList<>();
        // Get site
        // Save devices
        List<DeviceEntity> deviceEntities = deviceEntityRepository.saveAll(deviceMapper.mapToEntities(deviceList));
        return deviceMapper.mapToDto(deviceEntities);
    }
}
