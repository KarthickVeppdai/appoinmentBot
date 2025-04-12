package com.ChatBot.ChatBot.chat_configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQ {

    public static final String GPS_DATA_EXCHANGE = "whatsapp-msg-exchange";
    public static final String GPS_QUEUE = "whatsapp-queue";
    public static final String ROUTING_KEY_GPS = "whatsapp";

    @Bean
    public DirectExchange gpsDirectExchange() {
        return new DirectExchange(GPS_DATA_EXCHANGE);
    }

    @Bean
    public Queue queue() {
        return new Queue(GPS_QUEUE); // true for durable queue
    }

    @Bean
    public Binding gpsBinding() {
        return BindingBuilder.bind(queue()).to(gpsDirectExchange()).with(ROUTING_KEY_GPS);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
