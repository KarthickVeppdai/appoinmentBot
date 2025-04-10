package com.ChatBot.ChatBot.chat_service.intents;

import com.ChatBot.ChatBot.chat_configuration.OpenAI;
import com.ChatBot.ChatBot.chat_service.ai_service.WelcomeIntentAIService;
import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.UserContext;
import dev.langchain4j.model.output.structured.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ChatBot.ChatBot.chat_service.intents.WelcomeIntent.MainIntents.APPOINMENT;
import static com.ChatBot.ChatBot.chat_service.intents.WelcomeIntent.MainIntents.REPORT;

@Service("WELCOME")
public class WelcomeIntent implements IntentHandler {

    @Autowired
    public RedisService redisService;

    private UserContext saveContext;

    @Autowired
    public OpenAI openAI;

    enum MainIntents {


        APPOINMENT,


        REPORT,


        WELCOME,
    }

    @Override
    public Void IntentProcessor(Optional<UserContext> userContext, ProcessMessage processMessage) {


        userContext.filter(userContext1 -> userContext1.getCurrent_intent_status() == 0)
                .ifPresentOrElse(
                        userContext1 -> {
                            switch (openAI.getWelcomeIntent().isAnyManiIntent(processMessage.getBody())) {
                                case APPOINMENT:
                                    saveContext = new UserContext("APPOINMENT", 0,
                                            List.of("DOCTOR", "DATE", "SLOT"), List.of(0, 0, 0), false, 0, processMessage);
                                    System.out.println("Going to Appoinment");

                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                    //Welocme Please Choose doctor Name Send Doctor Information. help and ask for appointmnt to channel

                                    break;

                                case REPORT:
                                    saveContext = new UserContext("REPORT", 0,
                                            List.of("ID"), List.of(0), false, 0, processMessage);
                                    System.out.println("Going to Report");

                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                    //send msg to Medium

                                    break;


                                default:
                                    saveContext = new UserContext("WELCOME", 0,
                                            List.of(""), List.of(0), false, 0, processMessage);
                                    System.out.println("Going to Welcome because wrong input ");
                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                    //send msg to Medium "You entred Wrong Input Pleae entre Options Correctly"
                                    break;
                            }

                        },
                        () -> {

                            saveContext = new UserContext("WELCOME", 0,
                                    List.of(""), List.of(0), false, 0, processMessage);
                            System.out.println("Going to Welcome");

                            redisService.saveData(processMessage.getFrom(), saveContext);
                            //send msg like "you alreday completed any other help??"

                            //Say welcome Message with Confimartion and past chat details.
                            // You booked Docotr with hope select other options.
                        }

                );
        return null;
    }
}
