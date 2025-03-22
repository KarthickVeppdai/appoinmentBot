package com.ChatBot.ChatBot.chat_Processor;

import com.ChatBot.ChatBot.models.InComingMessage;
import com.ChatBot.ChatBot.models.InComingMessageEvent;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.ProcessMessageEvent;
import com.ChatBot.ChatBot.send_util.TImeStampConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WhatsAppMessageParser {

    @Autowired
    TImeStampConverter tImeStampConverter;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Async("myAsyncPoolTaskExecutor")
    @EventListener(condition = "#event.type.equals('incoming')")
    public void dispatchMessage(InComingMessageEvent event) throws JsonProcessingException {

        InComingMessage input_msg = (InComingMessage) event.getSource();


        System.out.println("Inside Diapstch");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configOverride(Boolean.class).setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.STRING));
        JsonNode node = mapper.readTree(input_msg.getMsg_body());
        Boolean has_entry = node.hasNonNull("entry");
        JsonNode message,statuses,value,changes,button,list;
        List<String> message_type,status_type;

        String msg_body,from,msg_id,button_text,list_text,recipient_id,interactive_type;


        changes = node.findValue("changes");
        if(changes !=null){
            message = node.findValue("messages");
            statuses = node.findValue("statuses");
            value = node.findValue("value");
            if(message !=null)
            {
                message_type=message.findValuesAsText("type");
                switch (message_type.isEmpty()?"nothing":message_type.get(0)) {
                    case "text" :
                        // till now not written --Product Inquiry Messages and Received Message Triggered by Click to WhatsApp Ads should be avoided
                        // write for exclusion case
                        applicationEventPublisher.publishEvent(new ProcessMessageEvent(new ProcessMessage(
                                message.findValuesAsText("from").getFirst(),
                                message.findValuesAsText("id").getFirst(),
                                message.findValuesAsText("body").getFirst(),
                                message.findValuesAsText("type").getFirst(),
                                tImeStampConverter.timeStampConverter(message.findValuesAsText("timestamp").getFirst())
                        ), "text"));
                        break;

                    case "reaction", "image", "sticker", "unknown","unsupported":
                        // send read recipt to that message id
                        // call send message function Ask for proper resoponse accoring to chat contxt
                        break;
                    case "button" :
                        from=message.findValuesAsText("from").get(0);
                        msg_id=message.findValuesAsText("id").get(0);
                        button = node.findValue("button");
                        button_text = button.findValuesAsText("text").get(0);
                        //   message_wta = new Message(from,msg_id,button_text,message_type.get(0));
                        break;
                    case "interactive","list_reply","button_reply":
                        from=message.findValuesAsText("from").get(0);
                        msg_id=message.findValuesAsText("id").get(0);
                        list = node.findValue("interactive");
                        list_text= list.findValuesAsText("title").get(0);
                        interactive_type= list.findValuesAsText("type").get(0);
                        //message_wta = new Message(from,msg_id,list_text,interactive_type);
                        // Write for 2 constions 1`.Received Answer to Reply Button and 2. Received Answer From List Message
                        break;
                    default :
                        // Location  and Contact Messages are avoided and no case is written ,handled by default.
                        // send read recipt to that message id
                        // call send message function Ask for proper resoponse accoring to chat contxt

                };
            }


            else if(statuses !=null)
            {
                status_type=statuses.findValuesAsText("status");
                switch (status_type.isEmpty()?"nothing":status_type.get(0)) {
                    case "sent" :
                        recipient_id = statuses.findValuesAsText("recipient_id").get(0);
                        System.out.println(recipient_id+"sent");
                        break;
                    case "read" :
                        recipient_id = statuses.findValuesAsText("recipient_id").get(0);
                        System.out.println(recipient_id+"read");
                        break;
                    case "failed" :
                        recipient_id = statuses.findValuesAsText("recipient_id").get(0);
                        System.out.println(recipient_id+"faoled");
                        break;
                    case "delivered":
                        recipient_id = statuses.findValuesAsText("recipient_id").get(0);
                        System.out.println(recipient_id+"delivered");
                        break;
                    default :
                        recipient_id = statuses.findValuesAsText("recipient_id").get(0);
                        System.out.println(recipient_id+"other");
                        // only 200 response, no read recipt

                };
            }


            else {// If it is no message and no status
                // only 200 response, no read recipt
                // send "someting went wrong" to user

            }
        }

        else {//if changes object not found
            // only 200 response, no read recipt
            // send "someting went wrong" to user

        }
        //only 200 response, no read recipt
        // send "someting went wrong" to user
        System.out.println("Dispatcher ends");

    }
}
