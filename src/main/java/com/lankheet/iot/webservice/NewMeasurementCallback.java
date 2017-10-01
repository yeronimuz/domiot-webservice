package com.lankheet.iot.webservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.lankheet.iot.datatypes.Measurement;
import com.lankheet.iot.webservice.utils.JsonUtil;

public class NewMeasurementCallback implements MqttCallback {
	private static final Logger LOG = LogManager.getLogger(NewMeasurementCallback.class);
	private MqttClientManager mqttClientManager;
	private DaoListener daoListener;

	public NewMeasurementCallback(MqttClientManager mqttClientManager, DaoListener dao) {
		this.mqttClientManager = mqttClientManager;
		this.daoListener = dao;
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOG.warn("Connection lost: {}", cause.getMessage());
		try {
			mqttClientManager.getClient().reconnect();
		} catch (MqttException e) {
			LOG.fatal("Could not reconnect: {}", e.getMessage());
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		LOG.info("Topic: {}, message: {}",topic, message.toString());
		String payload = message.getPayload().toString();
		Measurement measurement = JsonUtil.measurementFromJson(payload);
		daoListener.newMeasurement(measurement);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		LOG.info("Delivery complete: {}", token);

	}

}
