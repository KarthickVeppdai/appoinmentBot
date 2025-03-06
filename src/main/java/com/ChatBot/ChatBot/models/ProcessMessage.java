package com.ChatBot.ChatBot.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
@Getter
@Setter
public class ProcessMessage {

    private String from;
    private String msg_id;
    private String body;
    private String msg_type;
    private Date timestamp;

}
