package com.ignite.demo.controller;

import com.ignite.demo.twitter.CreateTweet;
import com.ignite.demo.twitter.GetTwitterTimeLine;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

import java.util.List;

@RestController
@RequestMapping(path = "/api/tweet")
public class TweetController {

    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createConsumer(@RequestParam String message) throws TwitterException {
        CreateTweet.create(message);
    }

    @GetMapping
    public List<String> getTimeLineInformation() throws TwitterException {
        return GetTwitterTimeLine.getTimeLine();
    }
}
