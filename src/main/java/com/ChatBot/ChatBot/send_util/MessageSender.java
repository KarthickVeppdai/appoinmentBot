package com.ChatBot.ChatBot.send_util;

import com.ChatBot.ChatBot.models.MessageOutput;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RabbitListener(queues = "whatsapp-queue")
public class MessageSender {

    @Autowired
    private MessageSenderService messageSenderService;

    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private ExternalHttpProperties externalHttpProperties;

    public JSONObject messageBody(MessageOutput messageOutput)
    {
        JSONObject outer = new JSONObject();
        JSONObject inner = new JSONObject();
        outer.put("messaging_product", "whatsapp");
        outer.put("recipient_type", "individual");
        outer.put("to", messageOutput.getSender_id());
        outer.put("type", "text");
        inner.put("preview_url", "false");
        inner.put("body", messageOutput.getBody());
        outer.put("text", inner);
        return outer;
    }

    public String sendPostRequest(MessageOutput messageOutput) {
        HttpPost post = new HttpPost(externalHttpProperties.getBaseurl());
        try {
            for (Map.Entry<String, String> entry : externalHttpProperties.getHeaders().entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
            post.setEntity(new StringEntity(messageBody(messageOutput).toString(), StandardCharsets.UTF_8));
            CloseableHttpResponse response = httpClient.execute(post);
            return String.valueOf(response.getCode());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            post = null;
        }
    }

    @RabbitHandler
    public void listen(MessageOutput messageOutput) {
        System.out.println("Sent Message::::"+messageOutput.getBody() + sendPostRequest(messageOutput));
    }
}
