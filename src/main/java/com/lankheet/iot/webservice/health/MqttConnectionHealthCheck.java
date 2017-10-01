package com.lankheet.iot.webservice.health;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.codahale.metrics.health.HealthCheck;
import com.lankheet.iot.webservice.MqttClientManager;

public class MqttConnectionHealthCheck extends HealthCheck {

	private MqttClientManager mqttClientManager;

	public MqttConnectionHealthCheck(MqttClientManager mqttClientManager) {
		this.mqttClientManager = mqttClientManager;
	}

	@Override
	protected Result check() {
		try {
			mqttClientManager.getClient().subscribe("test");
		} catch (MqttException e) {
			return Result.unhealthy("Mqtt client closed");
		}
		return Result.healthy();
	}

}
