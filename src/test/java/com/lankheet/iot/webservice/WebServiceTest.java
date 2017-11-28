/**
 * 
 */
package com.lankheet.iot.webservice;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.junit.BeforeClass;
import org.junit.Test;
import com.lankheet.iot.datatypes.Measurement;
import com.lankheet.iot.datatypes.MeasurementType;
import com.lankheet.iot.datatypes.Sensor;
import com.lankheet.iot.datatypes.SensorType;
import com.lankheet.iot.webservice.config.DatabaseConfig;
import com.lankheet.iot.webservice.testutils.TestUtils;

/**
 * Test for @link {@link WebService} A database is required An mqtt server is required
 */
public class WebServiceTest {
    private static final String PERSISTENCE_UNIT = "meas-pu";

    private static WebService webService = new WebService();
    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static MqttClient mqttClient;

    @BeforeClass
    public static void doSetup() throws Exception {
        // Start service
        webService.run("server", "src/test/resources/application.yml");

        DatabaseConfig dbConfig = webService.getConfiguration().getDatabaseConfig();
        // Overwrite production persistence unit settings with test values
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("javax.persistence.jdbc.driver", dbConfig.getDriver());
        properties.put("javax.persistence.jdbc.url", dbConfig.getUrl());
        properties.put("javax.persistence.jdbc.user", dbConfig.getUserName());
        properties.put("javax.persistence.jdbc.password", dbConfig.getPassword());

        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
        em = emf.createEntityManager();
        // Prepare database
//        Sensor sensor1 = new Sensor(SensorType.POWER_METER.getId(), "power-meter", "meterkast");
//        em.getTransaction().begin();
//        em.persist(sensor1);
//        em.getTransaction().commit();
        mqttClient = TestUtils.createMqttClientConnection();
    }

    @Test
    public void testEndToEndTest() throws Exception {
        Measurement measurement = new Measurement(1, new Date(), MeasurementType.ACTUAL_CONSUMED_POWER.getId(), 1.1);
        TestUtils.sendMqttMeasurement(mqttClient, measurement);
        assertTrue(mqttClient.isConnected());
        // Give the service some time to finish
        Thread.sleep(2000);
        Query query = em.createQuery("SELECT e FROM measurements e WHERE e.sensorId = " + measurement.getSensorId()
                + "AND e.type = " + measurement.getType() + " AND e.value = " + measurement.getValue());
        List<Measurement> resultList = query.getResultList();
        int numberOfDuplicates = resultList.size();
        assertThat(numberOfDuplicates, is(1));
    }
}
