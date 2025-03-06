package com.ChatBot.ChatBot.chat_controller;


import com.ChatBot.ChatBot.models.InComingMessage;
import com.ChatBot.ChatBot.models.InComingMessageEvent;
import com.ChatBot.ChatBot.send_util.TImeStampConverter;
import com.fasterxml.jackson.core.JsonProcessingException;


import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Date;


@RestController
@RequestMapping("/")
public class IncomingChatController {



    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/webhook")
    public String test1(@RequestParam(value = "hub.challenge", defaultValue = "0")String challange){
        System.out.println("body");
        return challange;
    }

    @PostMapping("/webhook")
    public ResponseEntity webhooktesting(@RequestBody String input_msg) throws JsonProcessingException, ParseException {
        InComingMessage inComingMessage = new InComingMessage();
        inComingMessage.setMsg_body(input_msg);
        applicationEventPublisher.publishEvent(new InComingMessageEvent(inComingMessage, "incoming"));
        System.out.println("Webhook Ends");
        return ResponseEntity.ok().build();
     }



}
