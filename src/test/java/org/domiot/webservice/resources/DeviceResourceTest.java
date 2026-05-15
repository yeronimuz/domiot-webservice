package org.domiot.webservice.resources;

import org.domiot.webservice.services.DeviceService;
import org.domiot.webservice.services.DuplicateDeviceException;
import org.domiot.model.Device;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceResourceTest {

    @Mock
    private DeviceService deviceService;

    @InjectMocks
    private DeviceResource deviceResource;

    @Test
    void addDevicesShouldPropagateDuplicateWhenServiceThrowsDuplicateDeviceException() {
        List<Device> input = List.of(new Device());
        when(deviceService.addDevices(1L, input)).thenThrow(new DuplicateDeviceException("duplicate"));

        assertThrows(DuplicateDeviceException.class, () -> deviceResource.addDevices(1L, input));
    }

    @Test
    void addDevicesShouldReturnNotFoundWhenServiceThrowsIllegalArgumentException() {
        List<Device> input = List.of(new Device());
        when(deviceService.addDevices(2L, input)).thenThrow(new IllegalArgumentException("unknown site"));

        ResponseEntity<List<Device>> response = deviceResource.addDevices(2L, input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void addDevicesShouldReturnOkWhenServiceSucceeds() {
        List<Device> input = List.of(new Device());
        List<Device> created = List.of(new Device());
        when(deviceService.addDevices(3L, input)).thenReturn(created);

        ResponseEntity<List<Device>> response = deviceResource.addDevices(3L, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(created, response.getBody());
    }
}

