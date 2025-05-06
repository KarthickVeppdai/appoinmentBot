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

    public static final String QUEUE_TWO = "whatsapp-msg-media-queue";
    public static final String EXCHANGE_TWO = "whatsapp-media-exchange";
    public static final String ROUTING_KEY_TWO = "whatsapp-media";

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
    public Queue queueTwo() {
        return new Queue(QUEUE_TWO);
    }

    @Bean
    public DirectExchange exchangeTwo() {
        return new DirectExchange(EXCHANGE_TWO);
    }

    @Bean
    public Binding bindingTwo(Queue queueTwo, DirectExchange exchangeTwo) {
        return BindingBuilder.bind(queueTwo).to(exchangeTwo).with(ROUTING_KEY_TWO);    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
