package org.domiot.webservice.services;

import lombok.extern.slf4j.Slf4j;
import org.domiot.webservice.repositories.SiteRespository;
import org.domiot.entities.SiteEntity;
import org.domiot.mapper.SiteMapper;
import org.domiot.model.Site;
import org.springframework.dao.DataIntegrityViolationException;
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

        SiteEntity mapped = siteMapper.map(site);
        if (mapped.getName() != null && siteRepository.existsByName(mapped.getName())) {
            throw new DuplicateSiteException("A site with this name already exists");
        }

        try {
            SiteEntity savedEntity = siteRepository.saveAndFlush(mapped);
            return siteMapper.map(savedEntity);
        } catch (DataIntegrityViolationException ex) {
            // Covers races where another request inserts the same unique value between check and insert.
            throw new DuplicateSiteException("A site with the same unique fields already exists", ex);
        }
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
