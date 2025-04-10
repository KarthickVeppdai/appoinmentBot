package com.ChatBot.ChatBot.Util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
@Configuration
public class UtilityConstants {

    @Bean
    public List<String> docotorsList() {
        return Arrays.asList("Saurav","Arun","Nishanth");
    }

    @Bean
    public List<String> soltsList() {
        return Arrays.asList("10 AM","11 AM","12 AM","1 PM","2 PM","3 PM","4 PM","5 PM","6 PM","7 PM");
    }

}
