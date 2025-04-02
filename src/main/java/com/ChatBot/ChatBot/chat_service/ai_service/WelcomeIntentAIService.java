package com.ChatBot.ChatBot.chat_service.ai_service;

import dev.langchain4j.model.output.structured.Description;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface WelcomeIntentAIService {


    enum MainIntents {

        @Description("Like 1 or Appoinment or 1.Appoinment")
        APPOINMENT,

        @Description("Like 1 or REPORT or 1.REPORT")
        REPORT,

        @Description("Any text other than enum APPOINMENT, REPORT ) ")
        WELCOME,
    }

        @UserMessage("Determine the following Input according to Description: {{it}}")
        @SystemMessage("Determine the input belongs to which enum according to description")
        MainIntents isAnyManiIntent(String issueDescription);




}
