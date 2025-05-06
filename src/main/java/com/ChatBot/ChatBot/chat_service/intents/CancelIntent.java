package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.chat_service.mangers.IntentRegistry;
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

@Service("CANCEL")
public class CancelIntent implements IntentHandler {
    @Autowired
    RedisService redisService;

    private UserContext saveContext;

    @Autowired
    public IntentRegistry intentRegistry;

    @Autowired
    public MessageOuboundPasser messageDispatcher;

    @Autowired
    public TextSupplyService textSupplyService;

    @Override
    public Void IntentProcessor(Optional<UserContext> userContext, ProcessMessage processMessage) {

        //Information, help and next what to do
        System.out.println("Inside to Cancel");


        redisService.deleteData(processMessage.getFrom());
        messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("cancle.intent.empty"), "", false, List.of("")));
        intentRegistry.assignIntent("WELCOME").IntentProcessor(Optional.empty(), processMessage);


        return null;
    }
}
