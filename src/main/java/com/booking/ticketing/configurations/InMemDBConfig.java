package com.booking.ticketing.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "inmemdb")
@Data
public class InMemDBConfig {
    private String registry;
}
