package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.models.UserContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("cancel_intent")
public class CancelIntent implements IntentHandler {
    @Override
    public String IntentProcessor(Optional<UserContext> userContext) {
        return "cancel_intent";
    }
}
