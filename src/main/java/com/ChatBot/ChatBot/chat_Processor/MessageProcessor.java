package com.ChatBot.ChatBot.chat_Processor;

import com.ChatBot.ChatBot.models.InComingMessageEvent;

import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.ProcessMessageEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MessageProcessor
{
    @Async("myAsyncPoolTaskExecutor")
    @EventListener(condition = "#event.type.equals('text')")
    public void processTextMessage(ProcessMessageEvent event)
    {
        ProcessMessage processMessage = (ProcessMessage) event.getSource();
        System.out.println("Inside Process Text Msg"+processMessage.getFrom()+"---"+processMessage.getTimestamp());
    }
}
