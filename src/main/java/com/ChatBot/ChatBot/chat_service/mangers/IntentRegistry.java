package com.ChatBot.ChatBot.chat_service.mangers;

public interface IntentRegistry {

    IntentHandler assignIntent(String intent_name);
}
