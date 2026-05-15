package org.domiot.webservice.resources;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.services.SiteService;
import org.domiot.api.SiteApi;
import org.domiot.model.Site;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class SiteResource implements SiteApi {

    private final SiteService siteService;

    public SiteResource(final SiteService siteService) {
        this.siteService = siteService;
    }

    @Override
    public ResponseEntity<Site> addSite(Site site) {
        if (site == null) {
            return ResponseEntity.badRequest().build();
        }
        log.info("Creating site");
        return ResponseEntity.ok(siteService.addSite(site));
    }

    @Override
    public ResponseEntity<Site> getSite(Long siteId) {
        return siteService.getSite(siteId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Site>> getSites() {
        return ResponseEntity.ok(siteService.getSites());
    }

    @Override
    public ResponseEntity<Site> updateSite(Long siteId, Site site) {
        if (site == null) {
            return ResponseEntity.badRequest().build();
        }
        return siteService.updateSite(siteId, site)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
