package com.ChatBot.ChatBot.send_util;

import com.ChatBot.ChatBot.miniIO_util.PdfUploaderService;
import com.ChatBot.ChatBot.models.MessageOutput;
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
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
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

    public JSONObject messageDocument(MessageOutput messageOutput,String media_id)
    {
        JSONObject outer = new JSONObject();
        JSONObject inner = new JSONObject();
        outer.put("messaging_product", "whatsapp");
        outer.put("recipient_type", "individual");
        outer.put("to", messageOutput.getSender_id());
        outer.put("type", "document");
        inner.put("id", media_id);
        inner.put("caption","Your Report");
        inner.put("filename",messageOutput.getSender_id());
        outer.put("text", inner);
        return outer;
    }

    public String sendPostRequestText(MessageOutput messageOutput) {
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

    public String sendPostRequestMedia(MessageOutput messageOutput) throws Exception {
        String media_id=null;
        media_id= uploadPdfToWhatsApp(messageOutput);
        HttpPost post = new HttpPost(externalHttpProperties.getBaseurl());
        try {
            for (Map.Entry<String, String> entry : externalHttpProperties.getHeaders().entrySet()) {
                post.setHeader(entry.getKey(), entry.getValue());
            }
            post.setEntity(new StringEntity(messageDocument(messageOutput,media_id).toString(), StandardCharsets.UTF_8));
            CloseableHttpResponse response = httpClient.execute(post);
            return String.valueOf(response.getCode());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            post = null;
            media_id=null;
        }
    }

    public String uploadPdfToWhatsApp(MessageOutput messageOutput) throws Exception {

        HttpPost post = new HttpPost("https://graph.facebook.com/v18.0/"+messageOutput.getSender_id()+"/media");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("messaging_product", "whatsapp");
        builder.addTextBody("type", "document");
        builder.addBinaryBody("file", pdfUploaderService.getPdfFromMinio("whatsapp-media",messageOutput.getSender_id()), ContentType.APPLICATION_OCTET_STREAM, messageOutput.getSender_id());

        List<String> values = externalHttpProperties.getHeaders().entrySet().stream()
                .filter(e -> e.getKey().equals("Authorization"))
                .map(Map.Entry::getValue)
                .toList();

        post.setHeader("Authorization", values.get(0));
        post.setEntity(builder.build());


        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(post);
        String json = null;
        try {
            json = EntityUtils.toString(response.getEntity());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JsonNode root = new ObjectMapper().readTree(json);
        return root.get("id").asText(); // media_id
    }


    @RabbitHandler
    public void listen(MessageOutput messageOutput) {
        System.out.println("Sent Message::::" + messageOutput.getBody() + sendPostRequestText(messageOutput));
    }
}
