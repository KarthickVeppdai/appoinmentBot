package com.ChatBot.ChatBot.chat_service.ai_service;

import dev.langchain4j.service.SystemMessage;

public interface InfoAIService {

    @SystemMessage({
            "Frame your answer in maximum 75 words"
    })
    String answer(String query);
}
