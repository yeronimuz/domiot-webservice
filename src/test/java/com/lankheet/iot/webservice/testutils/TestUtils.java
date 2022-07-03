package com.lankheet.iot.webservice.testutils;

import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.utils.JsonUtil;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TestUtils
{
   private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);


   public static void sendMqttMeasurements(MqttClient mqttClient, List<Measurement> measurements)
      throws Exception
   {
      for (Measurement measurement : measurements)
      {
         LOG.info(measurement.toString());
         sendMqttMeasurement(mqttClient, measurement);
      }
   }


   public static void sendMqttMeasurement(MqttClient mqttClient, Measurement measurement)
      throws Exception
   {
      MqttMessage msg = new MqttMessage();
      msg.setPayload(JsonUtil.toJson(measurement).getBytes());
      mqttClient.publish("test", msg);
   }


   public static MqttClient createMqttClientConnection()
      throws MqttSecurityException, MqttException
   {
      MqttClient mqttClient = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
      MqttConnectOptions options = new MqttConnectOptions();
      options.setConnectionTimeout(60);
      options.setKeepAliveInterval(60);
      options.setUserName("testuser");
      options.setPassword("testpassword".toCharArray());
      mqttClient.connect(options);
      return mqttClient;
   }
}
