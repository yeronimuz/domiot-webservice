package org.domiot.webservice.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;

/**
 * JsonMapper configuration
 */
@Configuration
public class JsonMapperConfig {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSX";

    /**
     * General object mapper for deserializing objects that are received through Mqtt
     *
     * @return The object mapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATETIME_FORMAT)));
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(module);
    }
}
