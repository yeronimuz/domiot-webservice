package org.domiot.webservice.services;

import org.domiot.webservice.repositories.SiteRespository;
import org.lankheet.domiot.mapper.SiteMapper;
import org.lankheet.domiot.model.Site;

public class SiteService {
    private final SiteRespository siteRepository;
    private final SiteMapper siteMapper;

    public SiteService(SiteRespository siteRespository, SiteMapper siteMapper) {
        this.siteRepository = siteRespository;
        this.siteMapper = siteMapper;
    }

    public Site save(Site site) {
        return siteMapper.map(siteRepository.save(siteMapper.map(site)));
    }
}
