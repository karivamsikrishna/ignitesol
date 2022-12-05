package com.ignite.demo.modal;

import java.util.List;

public class UserSubscribeRequest {

    private String userName;
    private List<String> hashtags;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }
}
