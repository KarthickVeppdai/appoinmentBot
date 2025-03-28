package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.models.UserContext;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration("welcome_intent")
public class WelcomeIntent implements IntentHandler {
    @Override
    public String IntentProcessor(Optional<UserContext> userContext) {
        return "welcome_intent";
    }
}
