package com.ChatBot.ChatBot.models;

import org.springframework.context.ApplicationEvent;

public class InComingMessageEvent extends ApplicationEvent {

    String type;

    public InComingMessageEvent(Object source, String type) {
        super(source);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
