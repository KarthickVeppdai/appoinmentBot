package com.ChatBot.ChatBot.chat_configuration;

import com.ChatBot.ChatBot.chat_service.ai_service.*;
import dev.langchain4j.model.openai.OpenAiChatModel;

import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.service.AiServices;
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
    public CancelIntentAIService getCancelIntent() {
        return AiServices.create(CancelIntentAIService.class, getModel());
    }

    @Bean
    public WelcomeIntentAIService getWelcomeIntent() {
        return AiServices.create(WelcomeIntentAIService.class, getModel());
    }

    @Bean
    public AppoinmentAIService getAppoinmentIntent() {
        return AiServices.create(AppoinmentAIService.class, getModel());
    }

    @Bean
    public ReportIntentAIService getReportIntent() {
        return AiServices.create(ReportIntentAIService.class, getModel());
    }

    @Bean
    public HelpIntentAIService getHelpIntent() {
        return AiServices.create(HelpIntentAIService.class, getModel());
    }
}
