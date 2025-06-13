package com.ChatBot.ChatBot.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
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
    private String last_intent;
    private Integer current_intent_status;
    private List<String> slots ;
    private List<Integer> slots_status;
    private Boolean slots_fullfilled ;
    private Integer current_slot_id;
    private Integer exception_status;
    private ProcessMessage processMessage;
    private LocalDateTime timestamp;

    // current_intent, current_intent_status, slots , slots_status,slots_fullfilled, current_slot_id,processMessage




}
