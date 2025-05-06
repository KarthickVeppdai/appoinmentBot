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
    }

    @Override
    public Void IntentProcessor(Optional<UserContext> userContext, ProcessMessage processMessage) {


        userContext
                .ifPresentOrElse(
                        userContext1 -> {

                            // Write One if condition when user completes intent.

                            switch (openAI.getWelcomeIntent().isAnyManiIntent(processMessage.getBody())) {
                                case APPOINMENT:
                                    saveContext = new UserContext("APPOINMENT", 0,
                                            List.of("DOCTOR", "DATE", "SLOT"), List.of(0, 0, 0), false, 0, processMessage);
                                    System.out.println("Inside Welcome passing to Appoinment");
                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                    StringBuilder sb = new StringBuilder(textSupplyService.getMessage("appointment") + "\n");
                                    for (String doctor : utilityConstants.docotorsList()) {
                                        sb.append("➡\uFE0F").append(doctor).append("\n");
                                    }
                                    messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), sb.toString(), "", false, List.of("")));
                                    break;

                                case REPORT:
                                    saveContext = new UserContext("REPORT", 0,
                                            List.of("ID"), List.of(0), false, 0, processMessage);
                                    System.out.println("::::::Setting Report Intent for 1 st Time:::::::");
                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                    messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("report"), "", false, List.of("")));

                                    break;


                                default:
                                    saveContext = new UserContext("WELCOME", 0,
                                            List.of(""), List.of(0), false, 0, processMessage);
                                    System.out.println("Going to Welcome because wrong input ");
                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                    //send msg to Medium "You entred Wrong Input Pleae entre Options Correctly"
                                    messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("welocmerepete"), "", false, List.of("")));
                                    messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("greeting"), "", false, List.of("")));
                                    break;
                            }

                        },
                        () -> {
                            // Called when new and cancel intent//

                            saveContext = new UserContext("WELCOME", 0,
                                    List.of(""), List.of(0), false, 0, processMessage);
                            System.out.println("Going to Welcome");
                            redisService.saveData(processMessage.getFrom(), saveContext);
                            messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("greeting"), "", false, List.of("")));

                            //send msg like "you alreday completed any other help??"

                            //Say welcome Message with Confimartion and past chat details.
                            // You booked Docotr with hope select other options.
                        }

                );
        return null;
    }
}
