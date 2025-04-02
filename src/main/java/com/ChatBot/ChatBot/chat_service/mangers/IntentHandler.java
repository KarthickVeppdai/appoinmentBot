package com.ChatBot.ChatBot.chat_service.mangers;

import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.UserContext;

import java.util.Optional;

public interface IntentHandler {

    public Void IntentProcessor(Optional<UserContext> userContext, ProcessMessage processMessage);



}
