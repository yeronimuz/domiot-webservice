package com.lankheet.iot.webservice.health;

import java.util.List;
import com.codahale.metrics.health.HealthCheck;
import com.lankheet.iot.datatypes.Measurement;
import com.lankheet.iot.webservice.DatabaseManager;

public class DatabaseHealthCheck extends HealthCheck {
	private DatabaseManager dbManager;

	public DatabaseHealthCheck(DatabaseManager dbManager) {
		this.dbManager = dbManager;
	}

	@Override
	protected Result check() {
	    Result result = Result.unhealthy("Nothing retrieved from database");
	    List<Measurement> measList = dbManager.getMeasurementsBySensor(1);
	    if (measList.size() > 0) {
			result =  Result.healthy("Healthy");
		}
		return result;
	}
}
