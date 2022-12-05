package com.ignite.demo.scheduler;

import com.google.gson.Gson;
import com.ignite.demo.configuration.UserInterestsConfig;
import com.ignite.demo.kafka.producer.KafkaProducer;
import com.ignite.demo.twitter.CreateTweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
//    @Autowired
//    private Map<String, List<String>> userSubscriptionInfo;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Scheduled(fixedRate = 10000)
    public void fixedRateSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Fixed Rate scheduler:: " + strDate);

        UserInterestsConfig.getUserSubscribedInfoInstance().entrySet().stream()
                .forEach(data -> {
                    data.getValue().forEach(userInterests -> {
                        try {
                            Status response = CreateTweet.create(data.getKey() + "-testing#" + userInterests);
                            kafkaProducer.send(data.getKey(), data.getKey() + "-" + new Gson().toJson(response));
                        } catch (TwitterException e) {
                            LOGGER.error(e.getErrorMessage());
                        }
                    });
                });

    }
}