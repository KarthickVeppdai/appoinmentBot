package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.Util.UtilityConstants;
import com.ChatBot.ChatBot.chat_configuration.OpenAI;
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

@Service("WELCOME")
public class WelcomeIntent implements IntentHandler {

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
    public UtilityConstants utilityConstants;

    private MessageOutput messageOutput;


    enum MainIntents {


        APPOINMENT,


        REPORT,


        WELCOME,

        INFO
    }

    @Override
    public Void IntentProcessor(Optional<UserContext> userContext, ProcessMessage processMessage) {

        // use text from text supplier or message.prperties or use form template. Here form template
        try {
            userContext.ifPresentOrElse(
                    userContext1 -> {

                        // Write One if condition when user completes intent.

                        switch (openAI.getWelcomeIntent().isAnyManiIntent(processMessage.getBody())) {
                            case APPOINMENT:
                                saveContext = UserContext.builder()
                                        .current_intent("APPOINMENT")
                                        .current_intent_status(0)
                                        .slots(List.of("DOCTOR", "DATE", "SLOT"))
                                        .slots_status(List.of(0, 0, 0))
                                        .slots_fullfilled(false)
                                        .current_slot_id(0)
                                        .processMessage(processMessage)
                                        .build();
                                redisService.saveData(processMessage.getFrom(), saveContext);
                                StringBuilder sb = new StringBuilder(textSupplyService.getMessage("appointment") + "\n");
                                for (String doctor : utilityConstants.docotorsList()) {
                                    sb.append("➡\uFE0F").append(doctor).append("\n");
                                }
                                messageDispatcher.sendMessage(
                                        MessageOutput.builder()
                                                .sender_id(processMessage.getFrom())
                                                .body(sb.toString())
                                                .is_template(false)
                                                .build());
                                break;

                            case REPORT:
                                saveContext = UserContext.builder()
                                        .current_intent("REPORT")
                                        .current_intent_status(0)
                                        .slots_fullfilled(false)
                                        .current_slot_id(0)
                                        .processMessage(processMessage)
                                        .build();
                                redisService.saveData(processMessage.getFrom(), saveContext);
                                messageDispatcher.sendMessage(
                                        MessageOutput.builder()
                                                .sender_id(processMessage.getFrom())
                                                .is_template(false)
                                                .body(textSupplyService.getMessage("report"))
                                                .build());
                                break;

                            case INFO:
                                saveContext = UserContext.builder()
                                        .current_intent("INFO")
                                        .current_intent_status(0)
                                        .slots_fullfilled(false)
                                        .current_slot_id(0)
                                        .processMessage(processMessage)
                                        .build();
                                redisService.saveData(processMessage.getFrom(), saveContext);
                                messageDispatcher.sendMessage(
                                        MessageOutput.builder()
                                                .sender_id(processMessage.getFrom())
                                                .is_template(false)
                                                .body(textSupplyService.getMessage("info"))
                                                .build());
                                break;

                            default:
                                saveContext = UserContext.builder()
                                        .current_intent("WELCOME")
                                        .current_intent_status(0)
                                        .slots_fullfilled(false)
                                        .current_slot_id(0)
                                        .processMessage(processMessage)
                                        .build();
                                redisService.saveData(processMessage.getFrom(), saveContext);
                                messageDispatcher.sendMessage(
                                        MessageOutput.builder()
                                                .sender_id(processMessage.getFrom())
                                                .is_template(true)
                                                .template_name("welocme_repete")
                                                .build());
                                break;
                        }
                    }, () -> {
                        // Called when fresh
                        saveContext = UserContext.builder()
                                .current_intent("WELCOME")
                                .current_intent_status(0)
                                .slots_fullfilled(false)
                                .current_slot_id(0)
                                .processMessage(processMessage)
                                .build();
                        redisService.saveData(processMessage.getFrom(), saveContext);
                        messageDispatcher.sendMessage(
                                MessageOutput.builder()
                                        .sender_id(processMessage.getFrom())
                                        .is_template(true)
                                        .template_name("welcome_intent")
                                        .build());
                    }
            );
            return null;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            saveContext = null;
        }
        return null;
    }
}
