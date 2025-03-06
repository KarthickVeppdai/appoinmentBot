package com.ChatBot.ChatBot.chat_controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenge")
public class WebhookChallenge {

    @GetMapping("/webhook")
    public String test1(@RequestParam(value = "hub.challenge", defaultValue = "0")String challange){
        System.out.println("body");
        return challange;
    }


}
