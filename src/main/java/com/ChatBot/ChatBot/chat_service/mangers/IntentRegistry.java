package com.ChatBot.ChatBot.chat_service.mangers;

public interface IntentRegistry {

    public IntentHandler assignIntent(String intent_name);
}
