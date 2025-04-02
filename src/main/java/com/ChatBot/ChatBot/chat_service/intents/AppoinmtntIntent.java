package com.ChatBot.ChatBot.chat_service.intents;


import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("APPOINMENT")
public class AppoinmtntIntent implements IntentHandler {


    @Override
    public Void IntentProcessor(Optional<UserContext> userContext , ProcessMessage processMessage) {


        return null;
    }
}
