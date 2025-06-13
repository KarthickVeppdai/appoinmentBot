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
            userContext = Optional.ofNullable(redisService.getData(processMessage.getFrom()));

            if (userContext.filter(ctx -> ctx.getException_status() == 1).isPresent()) {
                // This is for exception status .Do Nothing
                // Optionally send mail or SMS
                return;
            }
            if (openAI.getCancelIntent().isPositive(processMessage.getBody())) {// go to cancel intent  openAI.getCancelIntent().isPositive(processMessage.getBody())
                intentRegistry.assignIntent("CANCEL").IntentProcessor(Optional.empty(), processMessage);
            } else if (openAI.getHelpIntent().isPositive(processMessage.getBody())) {
                intentRegistry.assignIntent("HELP").IntentProcessor(Optional.empty(), processMessage);
            } else {
                userContext.ifPresentOrElse(
                        userContext1 -> {
                            intentRegistry.assignIntent(userContext.get().getCurrent_intent()).IntentProcessor(userContext, processMessage);
                        },
                        () -> {
                            intentRegistry.assignIntent("WELCOME").IntentProcessor(Optional.empty(), processMessage);
                        }
                );
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            userContext = null;
            intentHandler = null;
        }
    }
}
