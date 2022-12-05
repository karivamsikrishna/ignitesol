package com.ignite.demo.controller;

import com.ignite.demo.configuration.UserInterestsConfig;
import com.ignite.demo.kafka.listener.CustomKafkaListenerRegistrar;
import com.ignite.demo.modal.CustomKafkaListenerProperty;
import com.ignite.demo.modal.KafkaConsumerAssignmentResponse;
import com.ignite.demo.modal.KafkaConsumerResponse;
import com.ignite.demo.modal.UserSubscribeRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/user")
public class UserController {

//    @Autowired
//    private Map<String, List<String>> userSubscriptionInfo;

    @Autowired
    private CustomKafkaListenerRegistrar customKafkaListenerRegistrar;

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    @PostMapping(path = "/subscribe")
    public ResponseEntity subscribeUser(@RequestBody UserSubscribeRequest request) {
        if (request != null && StringUtils.isNotBlank(request.getUserName()) && request.getHashtags() != null && !request.getHashtags().isEmpty()) {
            UserInterestsConfig.getUserSubscribedInfoInstance().put(request.getUserName(), request.getHashtags());
            customKafkaListenerRegistrar.registerCustomKafkaListener(request.getUserName(),
                    CustomKafkaListenerProperty.builder()
                            .topic(request.getUserName())
                            .listenerClass("DynamicCustomMessageListener")
                            .build(),
                    true);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/un-subscribe")
    public ResponseEntity unSubscribeUser(@RequestParam String userName) {
        if (StringUtils.isNotBlank(userName)) {
            if(UserInterestsConfig.getUserSubscribedInfoInstance().containsKey(userName)) {
                UserInterestsConfig.getUserSubscribedInfoInstance().remove(userName);
                deactivateConsumer(userName);
            } else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getSubscribedUserInformation() {
        return new ResponseEntity<Map<String, List<String>>>(UserInterestsConfig.getUserSubscribedInfoInstance(), HttpStatus.OK);
    }

    @GetMapping(path = "/active/consumers")
    public List<KafkaConsumerResponse> getConsumerIds() {
        return kafkaListenerEndpointRegistry.getListenerContainerIds()
                .stream()
                .map(this::createKafkaConsumerResponse)
                .collect(Collectors.toList());
    }

    private KafkaConsumerResponse createKafkaConsumerResponse(String consumerId) {
        MessageListenerContainer listenerContainer =
                kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        return KafkaConsumerResponse.builder()
                .consumerId(consumerId)
                .groupId(listenerContainer.getGroupId())
                .listenerId(listenerContainer.getListenerId())
                .active(listenerContainer.isRunning())
                .assignments(Optional.ofNullable(listenerContainer.getAssignedPartitions())
                        .map(topicPartitions -> topicPartitions.stream()
                                .map(this::createKafkaConsumerAssignmentResponse)
                                .collect(Collectors.toList()))
                        .orElse(null))
                .build();
    }

    private KafkaConsumerAssignmentResponse createKafkaConsumerAssignmentResponse(
            TopicPartition topicPartition) {
        return KafkaConsumerAssignmentResponse.builder()
                .topic(topicPartition.topic())
                .partition(topicPartition.partition())
                .build();
    }

    private void deactivateConsumer(String consumerId) {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(consumerId);
        if (Objects.isNull(listenerContainer)) {
            throw new RuntimeException(String.format("Consumer with id %s is not found", consumerId));
        } else if (!listenerContainer.isRunning()) {
            throw new RuntimeException(String.format("Consumer with id %s is already stop", consumerId));
        } else {
            log.info("Stopping a consumer with id " + consumerId);
            listenerContainer.stop();
        }
    }
}
