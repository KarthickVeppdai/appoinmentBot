package com.ChatBot.ChatBot.chat_service.ai_service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface HelpIntentAIService {

    @UserMessage("Does {{it}} has a negative sentiment?")
    @SystemMessage("You determine user text have negative Sentiment like cancel,exit,never mind,forget it,stop,off,shut up.")
    boolean isPositive(@V("it")String it);
}
