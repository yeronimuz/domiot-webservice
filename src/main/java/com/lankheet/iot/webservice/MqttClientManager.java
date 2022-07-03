package com.lankheet.iot.webservice;

import com.lankheet.iot.webservice.config.MqttConfig;
import com.lankheet.iot.webservice.dao.DaoListener;
import io.dropwizard.lifecycle.Managed;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQTT client manager that subscribes to the domotics topics.
 *
 */
public class MqttClientManager implements Managed {
    private static final Logger LOG = LoggerFactory.getLogger(MqttClientManager.class);

    private final MqttClient         client;
    private final MqttConnectOptions options = new MqttConnectOptions();

    public MqttClientManager(MqttConfig mqttConfig, DaoListener dao) throws MqttException {
        String userName = mqttConfig.getUserName();
        String password = mqttConfig.getPassword();
        client = new MqttClient(mqttConfig.getUrl(), MqttClient.generateClientId());

        client.setCallback(new NewMeasurementCallback(this, dao));
        options.setConnectionTimeout(60);
        options.setKeepAliveInterval(60);
        options.setUserName(userName);
        options.setPassword(password.toCharArray());
    }

    /**
     * TODO: This method should be triggered by the database command table change in order to configure clients.<BR>
     * Possible commands: Command to switch something, Config to set a (set of) parameter(s), Measure:
     * Invoke a sensor to generate a new measurement
     * 
     * @param topic The command topic {CMD, CONFIG, MEASURE}
     */
    public void publish(String topic, MqttMessage message) throws MqttException {
        client.publish(topic, message);
    }

    @Override
    public void start() throws Exception {
        LOG.info("Connecting mqtt broker with options: {}", options);
        client.connect(options);
        client.subscribe("#", 0);
    }

    @Override
    public void stop() throws Exception {
        LOG.info("Closing mqtt connection...");
        client.disconnect();
    }

    public MqttClient getClient() {
        return client;
    }
}
