/**
 * Lankheet Solutions. Do whatever you like, but kindly include the source as a reference.
 */
package com.lankheet.iot.webservice.config;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;
import cucumber.api.java.After;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * Test for @link {@link WebServiceConfig}
 *
 */
public class WebServiceConfigTest {
    // Prepare the SUT
    private static class WebServiceConfigTester extends Application<WebServiceConfig> {
        private static WebServiceConfigTester instance = null;
        private WebServiceConfig wsConfig;

        private WebServiceConfigTester() {
            // Do not allow instantiation through constructor
        }

        @Override
        public void run(WebServiceConfig configuration, Environment environment) throws Exception {
            this.wsConfig = configuration;
        }

        public static WebServiceConfigTester getInstance() {
            if (instance == null) {
                instance = new WebServiceConfigTester();
            }
            return instance;
        }

    }

    @BeforeClass
    public static void setup() throws Exception {
        WebServiceConfigTester.getInstance().run("server", "src/test/resources/application.yml");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConfigDatabase() throws Exception {
        WebServiceConfigTester wsTester = WebServiceConfigTester.getInstance();
        DatabaseConfig databaseConfig = wsTester.wsConfig.getDatabaseConfig();
        assertThat(databaseConfig, is(notNullValue()));
        assertThat(databaseConfig.getDriver(), is("com.mysql.jdbc.Driver"));
        assertThat(databaseConfig.getUrl(), is("jdbc:mysql://localhost:3306/domotics"));
        assertThat(databaseConfig.getUserName(), is("testuser"));
        assertThat(databaseConfig.getPassword(), is("p@ssW0rd"));
    }

    @Test
    public void testConfigMqtt() {
        WebServiceConfigTester wsTester = WebServiceConfigTester.getInstance();
        
        MqttConfig mqttConfig = wsTester.wsConfig.getMqttConfig();
        assertThat(mqttConfig, is(notNullValue()));
        assertThat(mqttConfig.getUrl(), is("tcp://localhost:1883"));
        assertThat(mqttConfig.getUserName(), is("johndoe"));
        assertThat(mqttConfig.getPassword(), is ("secret"));
    }    
}
