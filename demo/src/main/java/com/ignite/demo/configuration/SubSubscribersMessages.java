package com.ignite.demo.configuration;

import java.util.HashMap;
import java.util.Map;


public class SubSubscribersMessages {

    public static Map<String, String> subscriberMessages;

    public static Map<String, String> getSubscriberMessagesInstance() {
        if(subscriberMessages == null) {
            subscriberMessages = new HashMap<>();
        }
        return subscriberMessages;
    }
}
