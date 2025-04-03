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
    public Void IntentProcessor(Optional<UserContext> userContext , ProcessMessage processMessage) {


        userContext.filter(userContext1 -> userContext1.getCurrent_intent_status()==0)
                .ifPresentOrElse(
                        userContext1 -> {
                    if(openAI.getWelcomeIntent().isAnyManiIntent(processMessage.getBody()).equals(WelcomeIntentAIService.MainIntents.APPOINMENT))
                    {

                        saveContext = new UserContext("WELCOME",0,
                                List.of("1"),List.of(1),false,0,processMessage);
                        System.out.println("Inside Welcome Intenet");

                        redisService.saveData(processMessage.getFrom(),saveContext);
                        //send msg to Medium
                        System.out.println("Inside Welcome Intenet++++Contrext Saved");

                    }
                            if(openAI.getWelcomeIntent().isAnyManiIntent(processMessage.getBody()).equals(WelcomeIntentAIService.MainIntents.REPORT))
                            {

                                saveContext = new UserContext("WELCOME",0,
                                        List.of("1"),List.of(1),false,0,processMessage);
                                System.out.println("Inside Welcome Intenet");

                                redisService.saveData(processMessage.getFrom(),saveContext);
                                //send msg to Medium
                                System.out.println("Inside Welcome Intenet++++Contrext Saved");

                            }

                            if(openAI.getWelcomeIntent().isAnyManiIntent(processMessage.getBody()).equals(WelcomeIntentAIService.MainIntents.WELCOME))
                            {

                                saveContext = new UserContext("WELCOME",0,
                                        List.of("1"),List.of(1),false,0,processMessage);
                                System.out.println("Inside Welcome Intenet");

                                redisService.saveData(processMessage.getFrom(),saveContext);
                                //send msg to Medium
                                System.out.println("Inside Welcome Intenet++++Contrext Saved");

                            }



                },
        ()->{
             //Say welcome Message with Confimartion and past chat details.
             // You booked Docotr with hope select other options.


        }
                                    );
        return null;
    }
}
