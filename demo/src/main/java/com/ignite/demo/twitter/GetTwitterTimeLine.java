package com.ignite.demo.twitter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.util.List;
import java.util.stream.Collectors;

public class GetTwitterTimeLine {

    public static List<String> getTimeLine() throws TwitterException {
        Twitter twitter = TwitterFactory.getSingleton();
        List<Status> statuses = twitter.getHomeTimeline();
        return statuses.stream().map(
                item -> item.getText()).collect(
                Collectors.toList());
    }
}
