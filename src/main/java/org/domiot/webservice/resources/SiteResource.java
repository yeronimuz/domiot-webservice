package org.domiot.webservice.resources;

import jakarta.validation.Valid;
import org.lankheet.domiot.api.SiteApi;
import org.lankheet.domiot.model.Site;

import java.util.List;

public class SiteResource implements SiteApi {
    @Override
    public Site addSite(@Valid Site site) {
        return null;
    }

    @Override
    public Site getSite(Long siteId) {
        return null;
    }

    @Override
    public List<Site> getSites() {
        return List.of();
    }

    @Override
    public Site updateSite(Long siteId, @Valid Site site) {
        return null;
    }
}
