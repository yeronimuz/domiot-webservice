package com.lankheet.iot.webservice.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class WebServiceConfig extends Configuration {

	@Valid
    @NotNull
    @JsonProperty
    private MqttConfig mqttConfig = new MqttConfig();

	@Valid
    @NotNull
    @JsonProperty
    private DatabaseConfig databaseConfig = new DatabaseConfig();

	public MqttConfig getMqttConfig() {
		return mqttConfig;
	}

	public void setMqttConfig(MqttConfig mqttConfig) {
		this.mqttConfig = mqttConfig;
	}

	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}

	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}
}
