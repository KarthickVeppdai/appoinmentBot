package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.chat_configuration.OpenAI;
import com.ChatBot.ChatBot.chat_configuration.PgVector;
import com.ChatBot.ChatBot.chat_service.ai_service.InfoAIService;
import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.models.MessageOutput;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.UserContext;
import com.ChatBot.ChatBot.send_util.MessageOuboundPasser;
import com.ChatBot.ChatBot.send_util.TextSupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("INFO")
public class InfoIntent implements IntentHandler {

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


        System.out.println("::::Inside Info Intent::::");
        saveContext = new UserContext("INFO", 0,
                List.of("ID"), List.of(0), false, 0, processMessage);
        redisService.saveData(processMessage.getFrom(), saveContext);
        messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), "Your Required Information are:", "", false, List.of("")));
        messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), pgVector.hospitalInfoRag().answer(processMessage.getBody()).toString(), "", false, List.of("")));
        messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("info.repeat"), "", false, List.of("")));
        return null;

    }
}
