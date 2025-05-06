package com.ChatBot.ChatBot.chat_service.ai_service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CancelIntentAIService {

    @SystemMessage("You are a Symmentic analysier")
    String cancelIntent(String userMessage);


    @UserMessage("Does {{it}} has a negative sentiment?")
    @SystemMessage("You determine user text have negative Sentiment like cancel,exit,never mind,forget it,stop,off,shut up.")
    boolean isPositive(@V("it")String it);

    @UserMessage("Does {{it}} has Main Menu?")
    @SystemMessage("You are determining the user text as Main Menu or not. Return true or false")
    boolean isMainMenuBack(@V("it")String text);

}
