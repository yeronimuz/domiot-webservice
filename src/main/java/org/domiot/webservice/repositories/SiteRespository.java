package org.domiot.webservice.repositories;

import org.domiot.entities.SiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRespository extends JpaRepository<SiteEntity, Long> {
	boolean existsByName(String name);
}
