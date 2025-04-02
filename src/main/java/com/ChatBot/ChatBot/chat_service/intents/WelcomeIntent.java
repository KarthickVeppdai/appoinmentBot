package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("WELCOME")
public class WelcomeIntent implements IntentHandler {

    @Autowired
    public RedisService redisService;

    private UserContext saveContext;

    @Override
    public Void IntentProcessor(Optional<UserContext> userContext , ProcessMessage processMessage) {

        saveContext = new UserContext();
        System.out.println("Inside Welcome Intenet");
        saveContext.setCurrent_intent("WELCOME");
        saveContext.setCurrent_intent_status(false);
      //  saveContext.setSlots(List.of());
       // saveContext.setSlots_status(List.of());
        saveContext.setCurrent_slot_id(0);
        saveContext.setSlots_fullfilled(false);
        saveContext.setProcessMessage(processMessage);

        redisService.saveData(processMessage.getFrom(),saveContext);
        //send msg to Medium
        System.out.println("Inside Welcome Intenet++++Contrext Saved");
        return null;
    }
}
