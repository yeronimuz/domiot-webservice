package org.domiot.webservice.services;

import org.domiot.webservice.repositories.DeviceEntityRepository;
import org.domiot.webservice.repositories.SiteRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lankheet.domiot.entities.DeviceEntity;
import org.lankheet.domiot.entities.SiteEntity;
import org.lankheet.domiot.mapper.DeviceMapper;
import org.lankheet.domiot.model.Device;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

	@Mock
	private DeviceEntityRepository deviceEntityRepository;
	@Mock
	private SiteRespository siteRepository;
	@Mock
	private DeviceMapper deviceMapper;

	@InjectMocks
	private DeviceService deviceService;

	@Test
	void addDevicesShouldThrowWhenSiteIdIsNull() {
		List<Device> devices = List.of(new Device());
		assertThrows(IllegalArgumentException.class, () -> deviceService.addDevices(null, devices));
	}

	@Test
	void addDevicesShouldReturnEmptyListWhenInputIsEmpty() {
		List<Device> result = deviceService.addDevices(1L, Collections.emptyList());

		assertEquals(0, result.size());
		verifyNoInteractions(siteRepository, deviceMapper, deviceEntityRepository);
	}

	@Test
	void addDevicesShouldSaveMappedEntitiesForSite() {
		Long siteId = 1L;
		SiteEntity siteEntity = new SiteEntity();
		List<Device> inputDevices = List.of(new Device());
		DeviceEntity mappedEntity = new DeviceEntity();
		List<DeviceEntity> mappedEntities = List.of(mappedEntity);
		DeviceEntity savedEntity = new DeviceEntity();
		List<DeviceEntity> savedEntities = List.of(savedEntity);
		List<Device> expected = List.of(new Device());

		when(siteRepository.findById(siteId)).thenReturn(Optional.of(siteEntity));
		when(deviceMapper.mapToEntities(inputDevices)).thenReturn(mappedEntities);
		when(deviceEntityRepository.saveAll(mappedEntities)).thenReturn(savedEntities);
		when(deviceMapper.mapToDto(savedEntities)).thenReturn(expected);

		List<Device> result = deviceService.addDevices(siteId, inputDevices);

		assertSame(expected, result);
		assertSame(siteEntity, mappedEntity.getSiteEntity());
		verify(deviceEntityRepository).saveAll(mappedEntities);
	}

	@Test
	void getDeviceShouldReturnMappedDeviceWhenFound() {
		DeviceEntity foundEntity = new DeviceEntity();
		Device mapped = new Device();

		when(deviceEntityRepository.findByIdAndSiteEntityId(10L, 5L)).thenReturn(Optional.of(foundEntity));
		when(deviceMapper.map(foundEntity)).thenReturn(mapped);

		List<Device> result = deviceService.getDevice(5L, 10L);

		assertEquals(1, result.size());
		assertSame(mapped, result.getFirst());
	}

	@Test
	void updateDeviceShouldPreserveExistingIdAndSite() {
		Long siteId = 3L;
		Long deviceId = 7L;
		Device deviceRequest = new Device();

		SiteEntity siteEntity = new SiteEntity();
		DeviceEntity existing = new DeviceEntity();
		existing.setId(deviceId);
		existing.setSiteEntity(siteEntity);

		DeviceEntity mapped = new DeviceEntity();
		DeviceEntity saved = new DeviceEntity();
		saved.setId(deviceId);
		saved.setSiteEntity(siteEntity);

		Device expectedDevice = new Device();

		when(deviceEntityRepository.findByIdAndSiteEntityId(deviceId, siteId)).thenReturn(Optional.of(existing));
		when(deviceMapper.map(deviceRequest)).thenReturn(mapped);
		when(deviceEntityRepository.save(any(DeviceEntity.class))).thenReturn(saved);
		when(deviceMapper.map(saved)).thenReturn(expectedDevice);

		List<Device> result = deviceService.updateDevice(siteId, deviceId, deviceRequest);

		ArgumentCaptor<DeviceEntity> captor = ArgumentCaptor.forClass(DeviceEntity.class);
		verify(deviceEntityRepository).save(captor.capture());
		assertEquals(deviceId, captor.getValue().getId());
		assertSame(siteEntity, captor.getValue().getSiteEntity());
		assertSame(expectedDevice, result.getFirst());
	}
}



