package com.ChatBot.ChatBot.chat_Processor;

import com.ChatBot.ChatBot.chat_configuration.OllamaAI;
import com.ChatBot.ChatBot.chat_configuration.OpenAI;
import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.chat_service.mangers.IntentRegistry;
import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.ProcessMessageEvent;
import com.ChatBot.ChatBot.models.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageDispatch {

    @Autowired
    public RedisService redisService;

    @Autowired
    public OpenAI openAI;

    @Autowired
    public OllamaAI ollamaAI;


    public IntentRegistry intentRegistry;
    @Autowired
    public MessageDispatch(IntentRegistry intentRegistry) {
        this.intentRegistry = intentRegistry;
    }

    private Optional<UserContext> userContext;

    private IntentHandler intentHandler;

    @EventListener(condition = "#event.type.equals('text')")
    public void processTextMessage(ProcessMessageEvent event) {

        try {
            ProcessMessage processMessage = (ProcessMessage) event.getSource();




            if (false) {// go to cancel intent
                intentHandler = intentRegistry.assignIntent("cancel_intent");
            } else {
                userContext = Optional.ofNullable(redisService.getData(processMessage.getFrom()));
                if (userContext.isPresent()) {
                    if (userContext.get().getCurrent_intent_status() == true) {
                        //Completed Pass to welcome Intent and in welocme intent process as old
                        intentRegistry.assignIntent("WELCOME").IntentProcessor(userContext,processMessage);
                    } else {
                        intentRegistry.assignIntent(userContext.get().getCurrent_intent()).IntentProcessor(userContext,processMessage);
                    }
                } else {//No context Available
                    IntentHandler intentHandler1 = intentRegistry.assignIntent("WELCOME");
                    intentHandler1.IntentProcessor(userContext,processMessage);
                }
            }
        } catch (Exception e) {
            System.out.println(e);

        } finally {
            System.out.println("Inside finally");
            userContext = null;
            intentHandler = null;
        }
    }
}
