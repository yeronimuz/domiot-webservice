package org.domiot.webservice.services;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.repositories.SiteRespository;
import org.lankheet.domiot.entities.SiteEntity;
import org.lankheet.domiot.mapper.SiteMapper;
import org.lankheet.domiot.model.Site;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SiteService {
    private final SiteRespository siteRepository;
    private final SiteMapper siteMapper;

    public SiteService(SiteRespository siteRespository, SiteMapper siteMapper) {
        this.siteRepository = siteRespository;
        this.siteMapper = siteMapper;
    }

    public Site addSite(Site site) {
        if (site == null) {
            throw new IllegalArgumentException("site cannot be null");
        }
        SiteEntity savedEntity = siteRepository.save(siteMapper.map(site));
        return siteMapper.map(savedEntity);
    }

    public Optional<Site> getSite(Long siteId) {
        return siteRepository.findById(siteId).map(siteMapper::map);
    }

    public List<Site> getSites() {
        return siteMapper.map(siteRepository.findAll());
    }

    public Optional<Site> updateSite(Long siteId, Site site) {
        if (site == null) {
            throw new IllegalArgumentException("site cannot be null");
        }
        Optional<SiteEntity> existing = siteRepository.findById(siteId);
        if (existing.isEmpty()) {
            log.info("No site found for update with siteId={}", siteId);
            return Optional.empty();
        }

        SiteEntity mapped = siteMapper.map(site);
        mapped.setId(siteId);
        if (mapped.getDtCreated() == null) {
            mapped.setDtCreated(existing.get().getDtCreated());
        }

        SiteEntity savedEntity = siteRepository.save(mapped);
        return Optional.of(siteMapper.map(savedEntity));
    }
}
