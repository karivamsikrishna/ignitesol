package com.ignite.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Configuration
public class UserInterestsConfig {

    public static Map<String, List<String>> userSubscribedInfo;

    public static Map<String, List<String>> getUserSubscribedInfoInstance() {
        if(userSubscribedInfo == null) {
            userSubscribedInfo = new HashMap<>();
        }
        return userSubscribedInfo;
    }
//    @Bean
//    @Scope(value = "singleton")
//    public Map<String, List<String>> userSubscribedInfo(){
//        return new HashMap<>();
//    }
}
