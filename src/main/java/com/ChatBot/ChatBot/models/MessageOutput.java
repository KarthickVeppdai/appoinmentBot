package com.ChatBot.ChatBot.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
@Getter
@Setter
public class MessageOutput implements Serializable {

    private String sender_id;
    private String body;
    private String template_name;
    private Boolean is_template;
    private List<String> template_slots;

    //sender_id,body,template_name,is_template,template_slots

}
