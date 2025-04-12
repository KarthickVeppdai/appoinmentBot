package com.ChatBot.ChatBot.chat_configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class TextConfig {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
       messageSource.setBasename("classpath:text/messages"); // folder + file base name
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600); // 1 hour caching
        messageSource.setFallbackToSystemLocale(false); // optional: disables OS default
        return messageSource;
    }
}
