package org.domiot.webservice.resources;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.services.DeviceService;
import org.lankheet.domiot.api.DeviceApi;
import org.lankheet.domiot.model.Device;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
public class DeviceResource implements DeviceApi {

    private final DeviceService deviceService;

    public DeviceResource(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public List<Device> addDevices(Long siteId, @Valid List<Device> deviceList) {
        return this.deviceService.addDevices(siteId, deviceList);
    }

    @Override
    public List<Device> getDevice(Long siteId, Long deviceId) {
        return List.of();
    }

    @Override
    public List<Device> getSiteDevices(BigDecimal bigDecimal) {
        return List.of();
    }

    @Override
    public List<Device> updateDevice(Long aLong, Object o, @Valid Device device) {
        return List.of();
    }
}
