package com.lankheet.iot.webservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.lankheet.iot.datatypes.Measurement;
import com.lankheet.iot.datatypes.MeasurementType;
import com.lankheet.iot.datatypes.Sensor;
import com.lankheet.iot.webservice.config.DatabaseConfig;
import com.lankheet.iot.webservice.dao.DaoListener;
import io.dropwizard.lifecycle.Managed;

public class DatabaseManager implements Managed, DaoListener {
    private static final Logger LOG = LogManager.getLogger(DatabaseManager.class);

    private static final String PERSISTENCE_UNIT = "meas-pu";
    private DatabaseConfig dbConfig;
    private EntityManagerFactory emf;
    private EntityManager em;
    /** Keep track of the last known value per sensor and per type; index in the list is sensorId */
    private List<HashMap<Integer, Double>> lastStoredValues = new ArrayList<HashMap<Integer, Double>>();

    public DatabaseManager(DatabaseConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    @Override
    public void start() throws Exception {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", dbConfig.getDriver());
        properties.put("javax.persistence.jdbc.url", dbConfig.getUrl());
        properties.put("javax.persistence.jdbc.user", dbConfig.getUserName());
        properties.put("javax.persistence.jdbc.password", dbConfig.getPassword());

        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
        em = emf.createEntityManager();
    }

    @Override
    public void stop() throws Exception {
        em.close();
        emf.close();
    }

    @Override
    public void newMeasurement(Measurement measurement) {
        if (!isRepeatedMeasurement(measurement)) {
            LOG.info("Storing: " + measurement);
            em.getTransaction().begin();
            em.persist(measurement);
            em.getTransaction().commit();
        } else {
            LOG.info("Ignoring repeated measurement: " + measurement);
        }
    }

    private boolean isRepeatedMeasurement(Measurement measurement) {
        boolean returnValue = false;
        int sensorId = measurement.getSensorId();
        int type = measurement.getType();
        double value = measurement.getValue();
        Measurement measLast = null;
        try {
            measLast =
                    (Measurement) em
                            .createQuery("SELECT e FROM measurements e WHERE e.sensorId = " + sensorId
                                    + " AND e.type = " + type + " order by e.id desc")
                            .setMaxResults(1).getSingleResult();
            returnValue = (value == measLast.getValue());
        } catch (NoResultException ex) {
            returnValue = false;
        }
        return returnValue;
    }

    @Override
    public List<Measurement> getMeasurementsBySensor(int sensorId) {
        return em.createQuery("SELECT e FROM measurements e WHERE e.sensorId = " + sensorId).getResultList();
    }

    @Override
    public List<Measurement> getMeasurementsBySensorAndType(int sensorId, int type) {
        return em.createQuery("SELECT e FROM measurements e WHERE e.sensorId = " + sensorId + " AND e.type = " + type)
                .getResultList();
    }
}
