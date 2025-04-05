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

import java.util.Objects;
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
            if (false) {// go to cancel intent  openAI.getCancelIntent().isPositive(processMessage.getBody())
                intentHandler = intentRegistry.assignIntent("CANCEL");
            } else {
                userContext = Optional.ofNullable(redisService.getData(processMessage.getFrom()));
                userContext.filter(Objects::nonNull)
                        .ifPresentOrElse(
                                userContext1 -> {

                                    Optional.ofNullable(userContext1.getCurrent_intent_status()).filter(i -> i == 0)
                                            .ifPresentOrElse(integer -> {
                                                        intentRegistry.assignIntent("WELCOME").IntentProcessor(userContext, processMessage);
                                                    },
                                                    () -> {
                                                        intentRegistry.assignIntent(userContext.get().getCurrent_intent()).IntentProcessor(userContext, processMessage);
                                                    });

                                },
                                () -> {
                                    intentRegistry.assignIntent("WELCOME").IntentProcessor(userContext, processMessage);
                                }
                        );
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
