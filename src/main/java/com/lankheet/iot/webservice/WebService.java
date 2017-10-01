package com.lankheet.iot.webservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lankheet.iot.webservice.config.WebServiceConfig;
import com.lankheet.iot.webservice.health.DatabaseHealthCheck;
import com.lankheet.iot.webservice.health.MqttConnectionHealthCheck;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * The web service has the following tasks:<BR>
 * <li>It accepts measurements from the message broker
 * <li>It saves the measurements in the database
 * <li>It serves as a resource for the measurements database
 *
 */
public class WebService extends Application<WebServiceConfig> {
	private static final Logger LOG = LogManager.getLogger(WebService.class);

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			LOG.error("Missing or wrong arguments");
		} else {
			new WebService().run(args[0], args[1]);
		}
	}

	@Override
	public void initialize(Bootstrap<WebServiceConfig> bootstrap) {
		LOG.info("Lankheet LNB IOT web service", "");
	}

	@Override
	public void run(WebServiceConfig configuration, Environment environment) throws Exception {
		DatabaseManager dbManager = new DatabaseManager(configuration.getDatabaseConfig());
		MqttClientManager mqttClientManager = new MqttClientManager(configuration.getMqttConfig(), dbManager);

		environment.getApplicationContext().setContextPath("/api");
		environment.lifecycle().manage(mqttClientManager);
		environment.lifecycle().manage(dbManager);
		// environment.jersey().register(measurementsResource);
		environment.healthChecks().register("database", new DatabaseHealthCheck(dbManager.getEntityManagerFactory()));
		environment.healthChecks().register("mqtt-server", new MqttConnectionHealthCheck(mqttClientManager));
	}

}
