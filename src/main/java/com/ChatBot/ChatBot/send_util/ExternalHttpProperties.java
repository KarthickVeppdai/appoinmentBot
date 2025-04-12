package com.ChatBot.ChatBot.send_util;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "http.external")
@Getter
@Setter
public class ExternalHttpProperties {

    private String baseurl;
    private Map<String, String> headers = new HashMap<>();
}
