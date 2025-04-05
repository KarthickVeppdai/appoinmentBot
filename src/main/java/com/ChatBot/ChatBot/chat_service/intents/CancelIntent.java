package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("CANCEL")
public class CancelIntent implements IntentHandler {
    @Autowired
    RedisService redisService;

    private UserContext saveContext;

    @Override
    public Void IntentProcessor(Optional<UserContext> userContext , ProcessMessage processMessage) {

        //Information, help and next what to do
        System.out.println("Inside to Cancel");
        redisService.deleteData(processMessage.getFrom());
        return null;
    }
}
