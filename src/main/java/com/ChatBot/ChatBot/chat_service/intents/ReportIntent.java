package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.models.UserContext;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("report_intent")
public class ReportIntent implements IntentHandler {
    @Override
    public String IntentProcessor(Optional<UserContext> userContext) {
        return "report_intent";
    }
}
