package org.domiot.webservice;

import lombok.extern.slf4j.Slf4j;
import org.domiot.mapper.ActuatorMapperImpl;
import org.domiot.mapper.DeviceMapperImpl;
import org.domiot.mapper.DomiotParameterMapperImpl;
import org.domiot.mapper.MqttConfigMapperImpl;
import org.domiot.mapper.MqttTopicMapperImpl;
import org.domiot.mapper.MqttTopicPathMapperImpl;
import org.domiot.mapper.SensorMapperImpl;
import org.domiot.mapper.SensorValueMapperImpl;
import org.domiot.mapper.SiteMapperImpl;
import org.domiot.mapper.SerialConfigMapperImpl;
import org.domiot.mapper.UserMapperImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 * Main SpringBoot application class
 */
@SpringBootApplication
@EnableJpaRepositories("org.domiot.entities")
@Import(
        {
                DeviceMapperImpl.class,
                SensorValueMapperImpl.class,
                SensorMapperImpl.class,
                SiteMapperImpl.class,
                UserMapperImpl.class,
                MqttTopicPathMapperImpl.class,
                MqttTopicMapperImpl.class,
                DomiotParameterMapperImpl.class,
                ActuatorMapperImpl.class,
                MqttConfigMapperImpl.class,
                SerialConfigMapperImpl.class,
        })
@Slf4j
public class DomiotWebServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DomiotWebServiceApplication.class)
                .run(args);
    }
}
