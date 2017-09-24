package com.lankheet.iot.webservice.health;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import com.codahale.metrics.health.HealthCheck;

public class DatabaseHealthCheck extends HealthCheck {
	private EntityManagerFactory emf;

	public DatabaseHealthCheck(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	protected Result check() throws Exception {
		int size;
		try {
			EntityManager em = emf.createEntityManager();
			Query query = em.createQuery("SELECT * from Measurements m");
			List<String> list = query.getResultList();
			size = list.size();
		} catch (IllegalStateException ex) {
			return Result.unhealthy(ex);
		}
		return Result.healthy("Nr of measurements: {}", size);
	}

}
