package com.ChatBot.ChatBot.chat_configuration;

import com.ChatBot.ChatBot.chat_service.mangers.IntentRegistry;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceLocatorConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean("intentRegistry")
    public ServiceLocatorFactoryBean  serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(IntentRegistry.class);
        return factoryBean;
    }
}
