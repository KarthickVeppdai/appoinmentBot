package com.ChatBot.ChatBot.chat_service.intents;

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

@Service("REPORT")
public class ReportIntent implements IntentHandler {

    @Autowired
    public RedisService redisService;

    private UserContext saveContext;

    @Autowired
    public OpenAI openAI;


    @Autowired
    public MessageOuboundPasser messageDispatcher;

    @Autowired
    public TextSupplyService textSupplyService;

    @Override
    public Void IntentProcessor(Optional<UserContext> userContext, ProcessMessage processMessage) {

        System.out.println("::::Inside report Intent::::");
        if (!userContext.get().getSlots_fullfilled()) {

            if (true)// check for vaild ID or phone no
            {
                // send pdf or send no pdf found
                saveContext = new UserContext("WELCOME", 0,
                        List.of(""), List.of(0), false, 0, processMessage);
                System.out.println("::::Sent report::::Going for Welcome::::");
                redisService.saveData(processMessage.getFrom(), saveContext);
                //send PDF/Document to user
                messageDispatcher.sendMessageMedia(new MessageOutput(processMessage.getFrom(), "Your Report", "", false, List.of("")));
               // messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("greeting"), "", false, List.of("")));
            } else {
                saveContext = new UserContext("REPORT", 1,
                        List.of("ID"), List.of(0), false, 0, processMessage);
                System.out.println("Request for Report ID");
                redisService.saveData(processMessage.getFrom(), saveContext);
                messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("report.repeat"), "", false, List.of("")));
            }
        } else {


            saveContext = new UserContext("WELCOME", 0,
                    List.of(""), List.of(0), false, 0, processMessage);
            System.out.println("Booking Confirmed-----Going to Welcome");
            redisService.saveData(processMessage.getFrom(), saveContext);
            messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("greeting"), "", false, List.of("")));
        }

        return null;
    }
}
