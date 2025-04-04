package com.ChatBot.ChatBot.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
@Getter
@Setter
public class InComingMessage implements Serializable {

    private String msg_body;

}
