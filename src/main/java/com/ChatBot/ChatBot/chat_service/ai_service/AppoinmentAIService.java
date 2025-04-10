package com.ChatBot.ChatBot.chat_service.ai_service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface AppoinmentAIService {

    @SystemMessage("You are a helpful assistant that classifies user input into one of the given options.If No/any Options matched classifi as 'notfound'")
    @UserMessage("Classify this input: {{input}} into one of: {{options}}")
    String findDoctor(@V("input")String input, @V("options")List<String> options);

    @SystemMessage({
            "You are a helpful assistant that extracts a date from user input.",
            "Today is: {{today}}.",
            "Return the date mentioned by the user, if any, in the format yyyy-MM-dd.",
            "If date metioned by user is same as {{today}}, respond with 1994-06-25",
            "Only include the date in your response. If no valid date is found, respond with 1994-06-25."
    })
    @UserMessage("Extract a date from this input: {{date}}. Only respond with a valid date in format yyyy-MM-dd.")
    String extractDate(@V("date")String date, @V("today")String today);

    @SystemMessage({
            "You are a helpful assistant that assigns user input to 1-hour time slots.",
            "Time slots start from 10:00 AM to 5:00 PM (last slot starts at 17:00).",
            "Respond ONLY with the start time of the matching slot in 24-hour format (HH:mm).",
            "If the input time is outside the range or unclear, return 00:00."
    })
    @UserMessage("Classify this input into a time slot: {{slot}}")
    String extractSlot(@V("slot")String input);

    @SystemMessage({
            "You are a helpful assistant that determines whether a user input is a confirmation.",
            "Respond with exactly one word: true for confirmation or false for rejection.",
            "Only respond with true or false."
    })
    @UserMessage("User said: {{input}}")
    Boolean classifyConfirmation(@V("input")String input);
}
