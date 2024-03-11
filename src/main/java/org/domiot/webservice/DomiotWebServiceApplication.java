package org.domiot.webservice;

import lombok.extern.slf4j.Slf4j;
import org.lankheet.domiot.mapper.DomiotParameterMapperImpl;
import org.lankheet.domiot.mapper.MqttTopicPathMapperImpl;
import org.lankheet.domiot.mapper.SensorMapperImpl;
import org.lankheet.domiot.mapper.SensorValueMapperImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main SpringBoot application class
 */
@SpringBootApplication
@EnableJpaRepositories("org.lankheet.domiot.entities")
@Import({SensorValueMapperImpl.class, SensorMapperImpl.class, MqttTopicPathMapperImpl.class, DomiotParameterMapperImpl.class})
@Slf4j
public class DomiotWebServiceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(DomiotWebServiceApplication.class)
                .run(args);
    }
}
