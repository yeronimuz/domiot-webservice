package org.domiot.webservice.services;

import org.domiot.webservice.repositories.SiteRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lankheet.domiot.entities.SiteEntity;
import org.lankheet.domiot.mapper.SiteMapper;
import org.lankheet.domiot.model.Site;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteServiceTest {

	@Mock
	private SiteRespository siteRepository;
	@Mock
	private SiteMapper siteMapper;

	@InjectMocks
	private SiteService siteService;

	@Test
	void addSiteShouldThrowWhenSiteIsNull() {
		assertThrows(IllegalArgumentException.class, () -> siteService.addSite(null));
	}

	@Test
	void addSiteShouldMapAndSave() {
		Site input = new Site();
		SiteEntity mapped = new SiteEntity();
		SiteEntity saved = new SiteEntity();
		Site expected = new Site();

		when(siteMapper.map(input)).thenReturn(mapped);
		when(siteRepository.save(mapped)).thenReturn(saved);
		when(siteMapper.map(saved)).thenReturn(expected);

		Site result = siteService.addSite(input);

		assertSame(expected, result);
	}

	@Test
	void getSiteShouldReturnMappedValueWhenFound() {
		SiteEntity entity = new SiteEntity();
		Site expected = new Site();

		when(siteRepository.findById(11L)).thenReturn(Optional.of(entity));
		when(siteMapper.map(entity)).thenReturn(expected);

		Optional<Site> result = siteService.getSite(11L);

		assertTrue(result.isPresent());
		assertSame(expected, result.get());
	}

	@Test
	void getSitesShouldReturnMappedList() {
		List<SiteEntity> entities = List.of(new SiteEntity(), new SiteEntity());
		List<Site> expected = List.of(new Site(), new Site());

		when(siteRepository.findAll()).thenReturn(entities);
		when(siteMapper.map(entities)).thenReturn(expected);

		List<Site> result = siteService.getSites();

		assertSame(expected, result);
	}

	@Test
	void updateSiteShouldReturnEmptyWhenUnknownSite() {
		when(siteRepository.findById(99L)).thenReturn(Optional.empty());

		Optional<Site> result = siteService.updateSite(99L, new Site());

		assertFalse(result.isPresent());
	}

	@Test
	void updateSiteShouldPreserveCreatedDateWhenMissingInPayload() {
		Long siteId = 12L;
		Date created = new Date();
		Site request = new Site();

		SiteEntity existing = new SiteEntity();
		existing.setId(siteId);
		existing.setDtCreated(created);

		SiteEntity mapped = new SiteEntity();
		SiteEntity saved = new SiteEntity();
		Site expected = new Site();

		when(siteRepository.findById(siteId)).thenReturn(Optional.of(existing));
		when(siteMapper.map(request)).thenReturn(mapped);
		when(siteRepository.save(any(SiteEntity.class))).thenReturn(saved);
		when(siteMapper.map(saved)).thenReturn(expected);

		Optional<Site> result = siteService.updateSite(siteId, request);

		ArgumentCaptor<SiteEntity> captor = ArgumentCaptor.forClass(SiteEntity.class);
		verify(siteRepository).save(captor.capture());
		assertEquals(siteId, captor.getValue().getId());
		assertEquals(created, captor.getValue().getDtCreated());
		assertTrue(result.isPresent());
		assertSame(expected, result.get());
	}
}

