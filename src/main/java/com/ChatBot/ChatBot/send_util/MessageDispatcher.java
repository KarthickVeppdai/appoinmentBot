package com.ChatBot.ChatBot.send_util;

import com.ChatBot.ChatBot.chat_configuration.RabbitMQ;
import com.ChatBot.ChatBot.models.MessageOutput;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageDispatcher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageOutput messageOutput) {
        rabbitTemplate.convertAndSend(RabbitMQ.GPS_DATA_EXCHANGE,RabbitMQ.ROUTING_KEY_GPS, messageOutput);
        System.out.println("Sent message: " + messageOutput.getSender_id());
    }
}
