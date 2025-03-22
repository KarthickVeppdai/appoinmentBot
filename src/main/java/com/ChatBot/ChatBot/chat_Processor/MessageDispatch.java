package com.ChatBot.ChatBot.chat_Processor;

import com.ChatBot.ChatBot.chat_configuration.OllamaAI;
import com.ChatBot.ChatBot.chat_configuration.OpenAI;
import com.ChatBot.ChatBot.chat_service.CancelIntent;
import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.ProcessMessageEvent;
import com.ChatBot.ChatBot.models.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageDispatch
{

    @Autowired
    public RedisService redisService;

    @Autowired
    public OpenAI openAI;

    @Autowired
    public OllamaAI ollamaAI;



    private UserContext userContext;


    @EventListener(condition = "#event.type.equals('text')")
    public void processTextMessage(ProcessMessageEvent event)
    {

        // check for cancel intent
        // get context and check timt to live etc..
        // pass to intent for further processing
        ProcessMessage processMessage = (ProcessMessage) event.getSource();
try {
    userContext = redisService.getData(processMessage.getFrom());
    System.out.println(openAI.getCancelIntent().isPositive(processMessage.getBody()));
}
catch (Exception e)
{

}
finally {
    System.out.println("Inside finally");
    userContext = null;

    processMessage=null;
}

    }
}
