package com.lankheet.iot.webservice;

import com.lankheet.iot.webservice.config.WebServiceConfig;
import com.lankheet.iot.webservice.health.DatabaseHealthCheck;
import com.lankheet.iot.webservice.health.MqttConnectionHealthCheck;
import com.lankheet.iot.webservice.resources.MeasurementsResource;
import com.lankheet.iot.webservice.resources.WebServiceInfo;
import com.lankheet.iot.webservice.resources.WebServiceInfoResource;
import com.lankheet.utils.TcpPortUtil;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The web service has the following tasks:<BR>
 * <li>It accepts measurements from the message broker
 * <li>It saves the measurements in the database
 * <li>It serves as a measurements resource for the measurements from the database
 *
 */
public class WebService extends Application<WebServiceConfig> {
    private static final Logger LOG                  = LoggerFactory.getLogger(WebService.class);
    private static final int    TIMEOUT_FOR_PORTSCAN = 200;
    private static final int DEFAULT_MQTT_PORT = 1883;
    private static final String DEFAULT_MQTT_HOST = "localhost";

    private WebServiceConfig configuration;

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            LOG.error("Missing or wrong arguments");
        } else {
            new WebService().run(args[0], args[1]);
        }
    }

    @Override
    public void initialize(Bootstrap<WebServiceConfig> bootstrap) {
        LOG.info("Lankheet IOT web service");
    }

    @Override
    public void run(WebServiceConfig configuration, Environment environment) throws Exception {
        this.setConfiguration(configuration);
        if (!TcpPortUtil.isPortOpen(DEFAULT_MQTT_HOST, DEFAULT_MQTT_PORT, TIMEOUT_FOR_PORTSCAN)) {
            LOG.error("Mqtt port not accessible");
            System.exit(-1);
        } else {
            LOG.info("MQTT port available");
        }
        DatabaseManager dbManager = new DatabaseManager(configuration.getDatabaseConfig());
        MqttClientManager mqttClientManager = new MqttClientManager(configuration.getMqttConfig(), dbManager);
        WebServiceInfoResource webServiceInfoResource = new WebServiceInfoResource(new WebServiceInfo());
        MeasurementsResource measurementsResource = new MeasurementsResource(dbManager);
        environment.getApplicationContext().setContextPath("/api");
        environment.lifecycle().manage(mqttClientManager);
        environment.lifecycle().manage(dbManager);
        environment.jersey().register(webServiceInfoResource);
        environment.jersey().register(measurementsResource);

        environment.healthChecks().register("database", new DatabaseHealthCheck(dbManager));
        environment.healthChecks().register("mqtt-server", new MqttConnectionHealthCheck(mqttClientManager));
    }

    public WebServiceConfig getConfiguration() {
        return configuration;
    }

    public void setConfiguration(WebServiceConfig configuration) {
        this.configuration = configuration;
    }
}
