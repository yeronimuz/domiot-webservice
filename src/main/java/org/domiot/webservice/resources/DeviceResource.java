package org.domiot.webservice.resources;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.services.DeviceService;
import org.lankheet.domiot.api.DeviceApi;
import org.lankheet.domiot.model.Device;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
public class DeviceResource implements DeviceApi {

    private final DeviceService deviceService;

    public DeviceResource(final DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public ResponseEntity<List<Device>> addDevices(Long siteId, List<Device> deviceList) {
        log.info("Adding {} devices for siteId={}", deviceList == null ? 0 : deviceList.size(), siteId);
        return ResponseEntity.ok(this.deviceService.addDevices(siteId, deviceList));
    }

    @Override
    public ResponseEntity<List<Device>> getDevice(Long siteId, Long deviceId) {
        log.info("Device lookup is not implemented yet for siteId={} and deviceId={}", siteId, deviceId);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<Device>> getSiteDevices(BigDecimal siteId) {
        log.info("Site device lookup is not implemented yet for siteId={}", siteId);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<Device>> updateDevice(Long siteId, Object deviceId, Device device) {
        log.info("Device update is not implemented yet for siteId={} and deviceId={}", siteId, deviceId);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
