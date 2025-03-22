package com.ChatBot.ChatBot.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
@Getter
@Setter
public class UserContext implements Serializable {

    private String current_intent;
    private Boolean current_intent_status;
    private List<String> slots ;
    private List<Integer> slots_status;
    private Boolean slots_fullfilled ;
    private Integer current_slot_id;
    private ProcessMessage processMessage;

}
