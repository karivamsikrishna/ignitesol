package com.ignite.demo.kafka.listener;

import com.ignite.demo.configuration.CustomKafkaListenerProperties;
import com.ignite.demo.modal.CustomKafkaListenerProperty;
import lombok.SneakyThrows;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomKafkaListenerRegistrar implements InitializingBean {

    @Autowired
    private CustomKafkaListenerProperties customKafkaListenerProperties;

    @Autowired
    private BeanFactory beanFactory;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @Autowired
    private KafkaListenerContainerFactory kafkaListenerContainerFactory;

    @Override
    public void afterPropertiesSet() {
        customKafkaListenerProperties.getListeners()
                .forEach(this::registerCustomKafkaListener);
    }

    public void registerCustomKafkaListener(String name, CustomKafkaListenerProperty customKafkaListenerProperty) {
        this.registerCustomKafkaListener(name, customKafkaListenerProperty, false);
    }

    @SneakyThrows
    public void registerCustomKafkaListener(String name, CustomKafkaListenerProperty customKafkaListenerProperty,
                                            boolean startImmediately) {
        String listenerClass = String.join(".", CustomKafkaListenerRegistrar.class.getPackage().getName(),
                customKafkaListenerProperty.getListenerClass());
        CustomMessageListener customMessageListener =
                (CustomMessageListener) beanFactory.getBean(Class.forName(listenerClass));
        kafkaListenerEndpointRegistry.registerListenerContainer(
                customMessageListener.createKafkaListenerEndpoint(name, customKafkaListenerProperty.getTopic()),
                kafkaListenerContainerFactory, startImmediately);
    }
}
