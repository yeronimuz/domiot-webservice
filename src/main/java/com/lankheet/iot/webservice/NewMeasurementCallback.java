package com.lankheet.iot.webservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lankheet.iot.datatypes.domotics.SensorValue;
import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.iot.datatypes.entities.MeasurementType;
import com.lankheet.iot.datatypes.entities.Sensor;
import com.lankheet.iot.datatypes.entities.SensorType;
import com.lankheet.iot.webservice.dao.DaoListener;
import com.lankheet.utils.JsonUtil;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Worker class of the Mqtt connection.
 */
public class NewMeasurementCallback implements MqttCallback
{
   private static final Logger            LOG = LoggerFactory.getLogger(NewMeasurementCallback.class);
   private final        MqttClientManager mqttClientManager;
   private final        DaoListener       daoListener;


   public NewMeasurementCallback(MqttClientManager mqttClientManager, DaoListener dao)
   {
      this.mqttClientManager = mqttClientManager;
      this.daoListener = dao;
   }


   @Override
   public void connectionLost(Throwable cause)
   {
      LOG.warn("Connection lost: {}", cause.getMessage());
      try
      {
         mqttClientManager.getClient().reconnect();
      }
      catch (MqttException e)
      {
         LOG.error("Could not reconnect: {}", e.getMessage());
      }
   }


   @Override
   public void messageArrived(String topic, MqttMessage message)
      throws Exception
   {
      LOG.info("Topic: {}, message: {}", topic, message);
      String payload = new String(message.getPayload());
      LOG.info("Payload: {}", payload);
      SensorValue sensorValue = null;
      try
      {
         sensorValue = JsonUtil.fromJson(payload);
      }
      catch (JsonProcessingException e)
      {
         LOG.error(e.getMessage());
      }
      LOG.info("Starting store action");
      daoListener.newMeasurement(mapSensorValueToMeasurement(sensorValue));
   }


   private Measurement mapSensorValueToMeasurement(SensorValue sensorValue)
   {
      return new Measurement(
         new Sensor(
            SensorType.getType(sensorValue.getSensorNode().getSensorType()),
            sensorValue.getSensorNode().getSensorMac(),
            null,
            null),
         sensorValue.getTimeStamp(),
         MeasurementType.getType(sensorValue.getMeasurementType()),
         sensorValue.getValue());
   }


   @Override
   public void deliveryComplete(IMqttDeliveryToken token)
   {
      LOG.info("Delivery complete: {}", token);
   }
}
