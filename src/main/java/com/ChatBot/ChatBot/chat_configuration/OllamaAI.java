package com.ChatBot.ChatBot.chat_configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaAI {


    private static final String BASE_URL = "http://localhost:11434";
    private static final String MODEL_NAME = "deepseek-r1:7b";

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        // Creates standard chat model for:
        // - Regular question-answering
        // - Single response generation
        // - Zero temperature for consistent outputs
        return OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .temperature(0.0)  // Deterministic responses
                .modelName(MODEL_NAME)
                .build();
    }
}
