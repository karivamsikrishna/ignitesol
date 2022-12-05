package com.ignite.demo.configuration;

import com.ignite.demo.modal.CustomKafkaListenerProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "custom.kafka")
public class CustomKafkaListenerProperties {

    private Map<String, CustomKafkaListenerProperty> listeners;
}
