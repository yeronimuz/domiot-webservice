package org.domiot.webservice.resources;

import jakarta.validation.Valid;
import org.lankheet.domiot.api.SiteApi;
import org.lankheet.domiot.model.Site;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class SiteResource implements SiteApi {
    @Override
    public ResponseEntity<Site> addSite(@Valid Site site) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Site> getSite(Long siteId) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<Site>> getSites() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Site> updateSite(Long siteId, @Valid Site site) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
