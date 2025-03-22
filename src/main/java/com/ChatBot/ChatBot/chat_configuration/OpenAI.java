package com.ChatBot.ChatBot.chat_configuration;

import com.ChatBot.ChatBot.chat_service.CancelIntent;
import dev.langchain4j.model.openai.OpenAiChatModel;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.spring.AiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAI {

    @Value("${api.key}")
    private String api_key;

    @Bean
    public OpenAiChatModel getModel() {
        return OpenAiChatModel.builder()
                .apiKey(api_key)
                .modelName("gpt-4o-mini")
                .build();
    }
    @Bean
    public CancelIntent getCancelIntent()
    {
        return AiServices.create(CancelIntent.class,getModel());

    }

}
