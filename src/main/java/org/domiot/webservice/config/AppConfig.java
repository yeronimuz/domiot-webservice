package org.domiot.webservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"org.domiot.webservice.repositories", "org.lankheet.domiot.entities"})
@ComponentScan(basePackages = "org.domiot.webservice")
@EntityScan("org.lankheet.domiot.entities")
class AppConfig {
}
