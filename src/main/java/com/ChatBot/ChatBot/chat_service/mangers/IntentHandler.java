package com.ChatBot.ChatBot.chat_service.mangers;

import com.ChatBot.ChatBot.models.UserContext;

import java.util.Optional;

public interface IntentHandler {

    public String IntentProcessor(Optional<UserContext> userContext);
}
