package org.domiot.webservice.resources;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.lankheet.domiot.api.SiteApi;
import org.lankheet.domiot.model.Site;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class SiteResource implements SiteApi {
    @Override
    public ResponseEntity<Site> addSite(@Valid Site site) {
        log.info("Site creation is not implemented yet");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Site> getSite(Long siteId) {
        log.info("Site lookup is not implemented yet for siteId={}", siteId);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<List<Site>> getSites() {
        log.info("Site listing is not implemented yet");
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public ResponseEntity<Site> updateSite(Long siteId, @Valid Site site) {
        log.info("Site update is not implemented yet for siteId={}", siteId);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
