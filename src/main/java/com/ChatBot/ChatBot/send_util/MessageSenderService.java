package com.ChatBot.ChatBot.send_util;


import com.ChatBot.ChatBot.models.MessageOutput;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class MessageSenderService {

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

    public String sendPostRequest(MessageOutput messageOutput) throws IOException {
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


    public String sendWelcome(String welocme) throws ParseException {
        System.out.println("Inside Welcome");
        String result, body;
        String tok = "Bearer EAAQjk2eLHbIBO5ApcdfZBjdS1QDtQmpatmV2ZAVly9p92HZC4Jpco47KZBqhhj9GKTOCCZBLBn4ZBGnLETGXUVglhtTkcQjqos4PGtDPPO3kDU9ymZBDe8Leyp4h10Rdn5CrgZCozkpCpCZCDcZCSpaP6q7g9HFGc6Yn7YeNFAaUqucZCklHLwGx1k0UoSR8AdHupheHfRzZBp8O7OUTB6ADAZBTk83tLTDnG";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost("https://graph.facebook.com/v19.0/284845538036793/messages");
        postRequest.addHeader("Authorization", tok);
        postRequest.addHeader("content-type", "application/json");
        JSONObject object = new JSONObject();
        object.put("messaging_product", "whatsapp");
        object.put("recipient_type", "individual");
        object.put("to", "919543249890");
        object.put("type", "text");
        JSONObject object1 = new JSONObject();
        object1.put("preview_url", "false");
        object1.put("body", welocme);
        object.put("text", object1);
        StringEntity userEntity = null;
        userEntity = new StringEntity(object.toString());
        postRequest.setEntity(userEntity);
        CloseableHttpResponse output = null;
        try {
            output = httpClient.execute(postRequest);
            result = EntityUtils.toString(output.getEntity());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String sendTemplate() {
        String result, body;
        String tok = "Bearer EAAQjk2eLHbIBO3GGJ9c0O1Nh5L6pWOQOcDsqo9DXeE00VOTlshSS6ZCj0u8LNG5WBGZCBZARQ4WLKkOtae5l5EtU6N9TbSdAQ8R7AV8kf0TBkmNlYpeHkWRr5QRLttbAYtZCkQv6PAzMnwCsBT7q77ljcvaJo4BojPiqfon8ZAPBoQsMrbX5kFzSpYyu8ButdbzbAxQIeTnTMMZCUEpzx3CMWLnb8ZD";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost("https://graph.facebook.com/v19.0/284845538036793/messages");
        postRequest.addHeader("content-type", "application/json");
        postRequest.addHeader("Authorization", tok);
        body = "{\n    \"messaging_product\": \"whatsapp\",\n    \"to\": \"919543249890\",\n    \"type\": \"template\",\n    \"template\": {\n        \"name\": \"class\",\n        \"language\": {\n            \"code\": \"en_US\"\n        }\n    }\n}";
        StringEntity userEntity = null;
        userEntity = new StringEntity(body.toString());
        postRequest.setEntity(userEntity);

        CloseableHttpResponse output = null;
        try {
            output = httpClient.execute(postRequest);
            result = EntityUtils.toString(output.getEntity());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
