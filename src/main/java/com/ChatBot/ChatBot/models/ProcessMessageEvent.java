package com.ChatBot.ChatBot.models;

import org.springframework.context.ApplicationEvent;

public class ProcessMessageEvent extends ApplicationEvent {

    String type;

    public ProcessMessageEvent(Object source, String type) {
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
