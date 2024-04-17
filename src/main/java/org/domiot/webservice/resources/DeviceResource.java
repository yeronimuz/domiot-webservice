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

    public DeviceResource(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public ResponseEntity<List<Device>> addDevices(Long siteId, List<Device> deviceList) {
        return new ResponseEntity<>(this.deviceService.addDevices(siteId, deviceList), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Device>> getDevice(Long siteId, Long deviceId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<Device>> getSiteDevices(BigDecimal siteId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<Device>> updateDevice(Long siteId, Object deviceId, Device device) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
