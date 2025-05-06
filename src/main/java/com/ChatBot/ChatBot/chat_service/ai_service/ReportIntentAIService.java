package com.ChatBot.ChatBot.chat_service.ai_service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ReportIntentAIService {

    @SystemMessage("You are a strict mobile number validator. Only validate 10-digit numbers. " +
            "Respond with 'true' or 'false' only, no explanations.")
    @UserMessage("Validate this mobile number: {{mobileNumber}}")
    Boolean validateMobileNumber(@V("mobileNumber")String mobileNumber);
}
