package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.chat_configuration.OpenAI;
import com.ChatBot.ChatBot.chat_configuration.PgVector;
import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.models.MessageOutput;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.UserContext;
import com.ChatBot.ChatBot.send_util.MessageOuboundPasser;
import com.ChatBot.ChatBot.send_util.TextSupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("HELP")
public class HelpIntent implements IntentHandler {

    @Autowired
    public RedisService redisService;

    private UserContext saveContext;

    @Autowired
    public OpenAI openAI;


    @Autowired
    public MessageOuboundPasser messageDispatcher;

    @Autowired
    public TextSupplyService textSupplyService;

    @Autowired
    public PgVector pgVector;
    @Override
    public Void IntentProcessor(Optional<UserContext> userContext, ProcessMessage processMessage) {

        saveContext = UserContext.builder()
                .current_intent("WELCOME")
                .current_intent_status(0)
                .slots_fullfilled(false)
                .last_intent("HELP")
                .processMessage(processMessage)
                .build();
        redisService.saveData(processMessage.getFrom(), saveContext);
        messageDispatcher.sendMessage(
                MessageOutput.builder()
                        .sender_id(processMessage.getFrom())
                        .is_template(false)
                        .body("I am Helping You!!!")
                        .build());
        return null;
    }
}
