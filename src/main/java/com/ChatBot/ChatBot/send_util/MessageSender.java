package com.ChatBot.ChatBot.send_util;

import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.miniIO_util.PdfUploaderService;
import com.ChatBot.ChatBot.models.MessageOutput;
import com.ChatBot.ChatBot.models.UserContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RabbitListener(queues = "whatsapp-queue")
public class MessageSender {

    @Autowired
    private MessageSenderService messageSenderService;

    @Autowired
    private PdfUploaderService pdfUploaderService;

    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private ExternalHttpProperties externalHttpProperties;

    @Autowired
    public RedisService redisService;

    private UserContext saveContext;

    private Optional<UserContext> userContext;

    public JSONObject messageBody(MessageOutput messageOutput) {
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


    public JSONObject messageBodyTemplate(MessageOutput messageOutput) {
        JSONObject outer = new JSONObject();
        JSONObject inner = new JSONObject();
        JSONObject inner1 = new JSONObject();
        outer.put("messaging_product", "whatsapp");
        outer.put("to", messageOutput.getSender_id());
        outer.put("type", "template");
        inner.put("name", messageOutput.getTemplate_name());
        inner.put("language", inner1.put("code", "en_IN"));
        outer.put("template", inner);
        return outer;
    }

    public JSONObject messageBodyTemplateParameters(MessageOutput messageOutput) {
        JSONObject message = new JSONObject();
        message.put("messaging_product", "whatsapp");
        message.put("to", messageOutput.getSender_id());
        message.put("type", "template");

        JSONObject language = new JSONObject();
        language.put("code", "en_IN");

        JSONArray parameters = new JSONArray();
        parameters.put(new JSONObject().put("type", "text").put("text", "John"));


        JSONObject component = new JSONObject();
        component.put("type", "body");
        component.put("parameters", parameters);

        JSONArray components = new JSONArray();
        components.put(component);

        JSONObject template = new JSONObject();
        template.put("name", "your_template_name");
        template.put("language", language);
        template.put("components", components);

        message.put("template", template);

        return message;
    }


    public String sendPostRequestText(MessageOutput messageOutput) {

        return "";
    }


    @RabbitHandler
    public void listen(MessageOutput messageOutput) {

        HttpPost post = new HttpPost(externalHttpProperties.getBaseurl());
        for (Map.Entry<String, String> entry : externalHttpProperties.getHeaders().entrySet()) {
            post.setHeader(entry.getKey(), entry.getValue());
        }
        if (messageOutput.getIs_template()) {
            post.setEntity(new StringEntity(messageBodyTemplate(messageOutput).toString(), StandardCharsets.UTF_8));
        } else {
            post.setEntity(new StringEntity(messageBody(messageOutput).toString(), StandardCharsets.UTF_8));
        }
        try {
            CloseableHttpResponse response = httpClient.execute(post);
        } catch (Exception ex) {
            userContext = Optional.ofNullable(redisService.getData(messageOutput.getSender_id()));
            saveContext = userContext.get();
            saveContext.setException_status(1);
            redisService.saveData(messageOutput.getSender_id(), saveContext);
            //log message not sent with respect to incoming message
        }
    }
}
