package org.domiot.webservice.resources;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.services.DeviceService;
import org.domiot.api.DeviceApi;
import org.domiot.model.Device;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@RestController
public class DeviceResource implements DeviceApi {

    private final DeviceService deviceService;

    public DeviceResource(final DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public ResponseEntity<List<Device>> addDevices(Long siteId, @RequestBody(required = false) List<Device> deviceList) {
        log.info("Adding {} devices for siteId={}", deviceList == null ? 0 : deviceList.size(), siteId);
        try {
            return ResponseEntity.ok(this.deviceService.addDevices(siteId, deviceList));
        } catch (IllegalArgumentException ex) {
            log.info("Device creation rejected for siteId={}: {}", siteId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override
    public ResponseEntity<List<Device>> getDevice(Long siteId, Long deviceId) {
        List<Device> devices = this.deviceService.getDevice(siteId, deviceId);
        if (devices.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(devices);
    }

    @Override
    public ResponseEntity<List<Device>> getSiteDevices(BigDecimal siteId) {
        try {
            return ResponseEntity.ok(this.deviceService.getSiteDevices(asLong(siteId)));
        } catch (ArithmeticException ex) {
            log.warn("Received non-integer siteId value: {} ({})", siteId, ex.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<List<Device>> updateDevice(Long siteId, Object deviceId, @RequestBody(required = false) Device device) {
        try {
            List<Device> updated = this.deviceService.updateDevice(siteId, asLong(deviceId), device);
            return ResponseEntity.ok(updated);
        } catch (NumberFormatException | ArithmeticException ex) {
            log.info("Invalid id format for siteId={} and deviceId={}: {}", siteId, deviceId, ex.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException ex) {
            log.info("Update rejected for siteId={} and deviceId={}: {}", siteId, deviceId, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private Long asLong(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("siteId cannot be null");
        }
        return value.setScale(0, RoundingMode.UNNECESSARY).longValueExact();
    }

    private Long asLong(Object value) {
        return switch (value) {
            case null -> throw new IllegalArgumentException("deviceId cannot be null");
            case Long longValue -> longValue;
            case Integer intValue -> intValue.longValue();
            case BigDecimal decimalValue -> asLong(decimalValue);
            case String stringValue -> Long.parseLong(stringValue);
            default ->
                    throw new IllegalArgumentException("Unsupported deviceId type: " + value.getClass().getSimpleName());
        };
    }
}
