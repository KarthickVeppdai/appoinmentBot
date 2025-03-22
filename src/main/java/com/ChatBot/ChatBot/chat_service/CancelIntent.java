package com.ChatBot.ChatBot.chat_service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface CancelIntent {

    @SystemMessage("You are a Symmentic analysier")
    String cancelIntent(String userMessage);


    @UserMessage("Does {{it}} has a negative sentiment?")
    @SystemMessage("Analyze the negative Sentiment like cancel,exit,never mind,forget it,stop,off,shut up")
    boolean isPositive(String text);

}
