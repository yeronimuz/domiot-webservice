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

    /**
     * General object mapper for deserializing objects that are received through Mqtt and served to clients
     *
     * @return The object mapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(module);
    }
}
