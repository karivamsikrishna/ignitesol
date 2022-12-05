package com.ignite.demo.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ignite.demo.configuration.SubSubscribersMessages;
import com.ignite.demo.configuration.UserInterestsConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import twitter4j.Status;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class SocketTextHandler extends TextWebSocketHandler {

    //	@Autowired
//	private Map<String, List<String>> userSubscriptionInfo;
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketTextHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException, InterruptedException {

        JsonObject jsonObject = new JsonParser().parse(message.getPayload()).getAsJsonObject();
        String userName = jsonObject.get("user").getAsString();

        LOGGER.info("=============================>"+userName);
        LOGGER.info("=============================>"+UserInterestsConfig.getUserSubscribedInfoInstance().size());
        LOGGER.info("=============================>"+UserInterestsConfig.getUserSubscribedInfoInstance().containsKey(userName));


        if (UserInterestsConfig.getUserSubscribedInfoInstance().containsKey(userName)) {
            while (session.isOpen()) {
                String stringMessage = SubSubscribersMessages.getSubscriberMessagesInstance().get(userName);
                if(StringUtils.isNotBlank(stringMessage)) {
                    JsonObject statusJsonObject = new JsonParser().parse(stringMessage).getAsJsonObject();
                    session.sendMessage(new TextMessage(statusJsonObject.get("text").getAsString()));
                }


                TimeUnit.SECONDS.sleep(20);
            }
        } else {
            session.sendMessage(new TextMessage("Provided " + userName + "  not subscribed to twitter stream!"));
        }


    }

}