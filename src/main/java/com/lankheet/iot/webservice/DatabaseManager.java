package com.lankheet.iot.webservice;

import com.lankheet.iot.datatypes.entities.Measurement;
import com.lankheet.iot.webservice.config.DatabaseConfig;
import com.lankheet.iot.webservice.dao.DaoListener;
import io.dropwizard.lifecycle.Managed;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager implements Managed, DaoListener
{
   private static final Logger LOG = LoggerFactory.getLogger(DatabaseManager.class);

   private static final String               PERSISTENCE_UNIT = "meas-pu";
   private final        DatabaseConfig       dbConfig;
   private              EntityManagerFactory emf;
   private       EntityManager        em;
   /**
    * Keep track of the last known value per sensor and per type; index in the list is sensorId
    */
   private final Map<Integer, Double> lastStoredValues = new HashMap<>();


   public DatabaseManager(DatabaseConfig dbConfig)
   {
      this.dbConfig = dbConfig;
   }


   @Override
   public void start()
      throws Exception
   {
      Map<String, String> properties = new HashMap<>();
      properties.put("jakarta.persistence.jdbc.driver", dbConfig.getDriver());
      properties.put("jakarta.persistence.jdbc.url", dbConfig.getUrl());
      properties.put("jakarta.persistence.jdbc.user", dbConfig.getUserName());
      properties.put("jakarta.persistence.jdbc.password", dbConfig.getPassword());

      emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
      em = emf.createEntityManager();
   }


   @Override
   public void stop()
      throws Exception
   {
      em.close();
      emf.close();
   }


   @Override
   public void newMeasurement(Measurement measurement)
   {
      if (!isRepeatedMeasurement(measurement))
      {
         LOG.info("Storing: {}", measurement);
         em.getTransaction().begin();
         em.persist(measurement);
         em.getTransaction().commit();

         lastStoredValues.put(measurement.getSensor().getId(), measurement.getValue());
      }
      else
      {
         LOG.info("Ignoring repeated measurement: {}", measurement);
      }
   }


   private boolean isRepeatedMeasurement(Measurement measurement)
   {
      return lastStoredValues.containsKey(measurement.getSensor().getId()) && lastStoredValues.containsValue(measurement.getValue());
   }


   @Override
   public List<Measurement> getMeasurementsBySensor(int sensorId)
   {
      return em.createQuery("SELECT e FROM measurements e WHERE e.sensorId = " + sensorId).getResultList();
   }


   @Override
   public List<Measurement> getMeasurementsBySensorAndType(int sensorId, int type)
   {
      return em.createQuery("SELECT e FROM measurements e WHERE e.sensorId = " + sensorId + " AND e.type = " + type)
         .getResultList();
   }
}
