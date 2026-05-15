package org.domiot.webservice.resources;

import org.domiot.webservice.services.DuplicateSiteException;
import org.domiot.webservice.services.SiteService;
import org.domiot.model.Site;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SiteResourceTest {

    @Mock
    private SiteService siteService;

    @InjectMocks
    private SiteResource siteResource;

    @Test
    void addSiteShouldReturnBadRequestWhenSiteIsNull() {
        ResponseEntity<Site> response = siteResource.addSite(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(siteService, never()).addSite(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void addSiteShouldPropagateDuplicateWhenServiceThrowsDuplicateSiteException() {
        Site input = new Site();
        when(siteService.addSite(input)).thenThrow(new DuplicateSiteException("duplicate"));

        assertThrows(DuplicateSiteException.class, () -> siteResource.addSite(input));
    }

    @Test
    void addSiteShouldReturnOkWhenServiceSucceeds() {
        Site input = new Site();
        Site created = new Site();
        when(siteService.addSite(input)).thenReturn(created);

        ResponseEntity<Site> response = siteResource.addSite(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(created, response.getBody());
    }
}

