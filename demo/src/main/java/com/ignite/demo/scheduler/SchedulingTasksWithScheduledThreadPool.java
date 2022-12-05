package com.ignite.demo.scheduler;

import com.google.gson.Gson;
import com.ignite.demo.configuration.UserInterestsConfig;
import com.ignite.demo.kafka.producer.KafkaProducer;
import com.ignite.demo.twitter.CreateTweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class SchedulingTasksWithScheduledThreadPool implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingTasksWithScheduledThreadPool.class);

//    @Autowired
//    private Map<String, List<String>> userSubscriptionInfo;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Override
    public void run(String... args) throws Exception {
        Runnable task1 = () -> {
            LOGGER.info("Executing the task1 at: " + new Date());
            UserInterestsConfig.getUserSubscribedInfoInstance().entrySet().stream()
                    .forEach(data -> {
                        data.getValue().forEach(userInterests -> {
                            try {
                                Status response = CreateTweet.create(data.getKey() + "-testing-"+ new Random().nextInt() +"#" + userInterests);
                                kafkaProducer.send(data.getKey(), new Gson().toJson(response).toString());
                            } catch (TwitterException e) {
                                LOGGER.error(e.getErrorMessage());
                            }
                        });
                    });
        };
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
        while (true) {
            scheduledExecutorService.schedule(task1, 10, TimeUnit.SECONDS);
            TimeUnit.SECONDS.sleep(30);
        }
    }

}