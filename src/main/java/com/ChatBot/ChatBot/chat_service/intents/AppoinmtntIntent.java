package com.ChatBot.ChatBot.chat_service.intents;


import com.ChatBot.ChatBot.Util.UtilityConstants;
import com.ChatBot.ChatBot.chat_configuration.OpenAI;
import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.models.MessageOutput;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.UserContext;
import com.ChatBot.ChatBot.send_util.MessageOuboundPasser;
import com.ChatBot.ChatBot.send_util.TextSupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service("APPOINMENT")
public class AppoinmtntIntent implements IntentHandler {

    String doctor, date, slot;

    public Optional<LocalDate> extracted_date;
    public LocalDate user_date, today, sevenDaysFromNow;
    public LocalTime slotTime, now;

    @Autowired
    public RedisService redisService;

    private UserContext saveContext;

    @Autowired
    public OpenAI openAI;

    @Autowired
    public UtilityConstants utilityConstants;

    @Autowired
    public MessageOuboundPasser messageDispatcher;

    @Autowired
    public TextSupplyService textSupplyService;

    @Override
    public Void IntentProcessor(Optional<UserContext> userContext, ProcessMessage processMessage) {
        try {
            userContext.filter(userContext1 -> userContext1.getSlots_fullfilled() == false)
                    .ifPresentOrElse(
                            userContext1 -> {
                                switch (userContext.get().getCurrent_slot_id()) {
                                    case 0:
                                        doctor = openAI.getAppoinmentIntent().findDoctor(processMessage.getBody(), utilityConstants.docotorsList());
                                        if (!doctor.equalsIgnoreCase("notfound")) {
                                            saveContext = UserContext.builder()
                                                    .current_intent("APPOINMENT")
                                                    .current_intent_status(0)
                                                    .slots(List.of(doctor, "DATE", "SLOT"))
                                                    .slots_status(List.of(1, 0, 0))
                                                    .slots_fullfilled(false)
                                                    .current_slot_id(1)
                                                    .processMessage(processMessage)
                                                    .build();
                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                            messageDispatcher.sendMessage(
                                                    MessageOutput.builder()
                                                            .sender_id(processMessage.getFrom())
                                                            .is_template(true)
                                                            .template_name("date_slot")
                                                            .build());

                                        } else {
                                            saveContext = UserContext.builder()
                                                    .current_intent("APPOINMENT")
                                                    .current_intent_status(0)
                                                    .slots(List.of("DOCTOR", "DATE", "SLOT"))
                                                    .slots_status(List.of(0, 0, 0))
                                                    .slots_fullfilled(false)
                                                    .current_slot_id(0)
                                                    .processMessage(processMessage)
                                                    .build();
                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                            messageDispatcher.sendMessage(
                                                    MessageOutput.builder()
                                                            .sender_id(processMessage.getFrom())
                                                            .body(textSupplyService.getMessage("repeat.doctor"))
                                                            .is_template(false)
                                                            .build());
                                        }
                                        break;
                                    case 1:
                                        date = openAI.getAppoinmentIntent().extractDate(processMessage.getBody(), LocalDate.now(ZoneId.of("Asia/Kolkata")).toString());
                                        extracted_date = Optional.ofNullable(LocalDate.parse(date));
                                        extracted_date.filter(extracted_date -> (!extracted_date.isBefore(LocalDate.now()) && !extracted_date.isAfter(LocalDate.now().plusDays(7))))
                                                .ifPresentOrElse(
                                                        extracted_date -> {
                                                            saveContext = UserContext.builder()
                                                                    .current_intent("APPOINMENT")
                                                                    .current_intent_status(0)
                                                                    .slots(List.of(userContext1.getSlots().get(0), extracted_date.toString(), "SLOT"))
                                                                    .slots_status(List.of(1, 1, 0))
                                                                    .slots_fullfilled(false)
                                                                    .current_slot_id(2)
                                                                    .processMessage(processMessage)
                                                                    .build();
                                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                                            messageDispatcher.sendMessage(
                                                                    MessageOutput.builder()
                                                                            .sender_id(processMessage.getFrom())
                                                                            .is_template(true)
                                                                            .template_name("time_slot")
                                                                            .build());
                                                        },
                                                        () -> {
                                                            saveContext = UserContext.builder()
                                                                    .current_intent("APPOINMENT")
                                                                    .current_intent_status(0)
                                                                    .slots(List.of(userContext1.getSlots().get(0), "DATE", "SLOT"))
                                                                    .slots_status(List.of(1, 0, 0))
                                                                    .slots_fullfilled(false)
                                                                    .current_slot_id(1)
                                                                    .processMessage(processMessage)
                                                                    .build();
                                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                                            messageDispatcher.sendMessage(
                                                                    MessageOutput.builder()
                                                                            .sender_id(processMessage.getFrom())
                                                                            .is_template(true)
                                                                            .template_name("date_slot_repeat")
                                                                            .build());

                                                        }
                                                );
                                        break;
                                    case 2:
                                        slot = openAI.getAppoinmentIntent().extractSlot(processMessage.getBody());
                                        slotTime = LocalTime.parse(slot);
                                        now = LocalTime.now();
                                        if (!(slotTime.getHour() == 00)) {
                                            saveContext = UserContext.builder()
                                                    .current_intent("APPOINMENT")
                                                    .current_intent_status(0)
                                                    .slots(List.of(userContext1.getSlots().get(0), userContext1.getSlots().get(1), slotTime.toString()))
                                                    .slots_status(List.of(1, 1, 1))
                                                    .slots_fullfilled(true)
                                                    .current_slot_id(2)
                                                    .processMessage(processMessage)
                                                    .build();
                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                            messageDispatcher.sendMessage(
                                                    MessageOutput.builder()
                                                            .sender_id(processMessage.getFrom())
                                                            .body(textSupplyService.getAppointmentConfirmation("confirm", userContext1.getSlots().get(0),
                                                                    userContext1.getSlots().get(1), slotTime.toString()))
                                                            .is_template(false)
                                                            .build());

                                        } else {

                                            saveContext = UserContext.builder()
                                                    .current_intent("APPOINMENT")
                                                    .current_intent_status(0)
                                                    .slots(List.of(userContext1.getSlots().get(0), userContext1.getSlots().get(1), "SLOT"))
                                                    .slots_status(List.of(1, 1, 0))
                                                    .slots_fullfilled(false)
                                                    .current_slot_id(2)
                                                    .processMessage(processMessage)
                                                    .build();
                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                            messageDispatcher.sendMessage(
                                                    MessageOutput.builder()
                                                            .sender_id(processMessage.getFrom())
                                                            .is_template(true)
                                                            .template_name("time_slot_repeat")
                                                            .build());
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            },
                            //ask for confirmation if all slots fullfilled
                            () -> {
                                Optional.ofNullable(openAI.getAppoinmentIntent().classifyConfirmation(processMessage.getBody()))
                                        .filter(conformation -> conformation)
                                        .ifPresentOrElse(
                                                success -> {
                                                    //Save to Appoinment Table
                                                    //Send Message You Booked and Appoinemt Confirmed

                                                    saveContext = UserContext.builder()
                                                            .current_intent("WELCOME")
                                                            .current_intent_status(0)
                                                            .slots_fullfilled(false)
                                                            .processMessage(processMessage)
                                                            .build();
                                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                                    messageDispatcher.sendMessage(
                                                            MessageOutput.builder()
                                                                    .sender_id(processMessage.getFrom())
                                                                    .is_template(true)
                                                                    .template_name("booking_done")
                                                                    .build());
                                                },
                                                () -> {
                                                    saveContext = UserContext.builder()
                                                            .current_intent("APPOINMENT")
                                                            .current_intent_status(0)
                                                            .slots(List.of("DOCTOR", "DATE", "SLOT"))
                                                            .slots_status(List.of(0, 0, 0))
                                                            .slots_fullfilled(false)
                                                            .current_slot_id(0)
                                                            .processMessage(processMessage)
                                                            .build();
                                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                                    StringBuilder sb = new StringBuilder(textSupplyService.getMessage("booking.cancel") + "\n" + textSupplyService.getMessage("appointment") + "\n");
                                                    for (String doctor : utilityConstants.docotorsList()) {
                                                        sb.append("➡\uFE0F").append(doctor).append("\n");
                                                    }
                                                    messageDispatcher.sendMessage(
                                                            MessageOutput.builder()
                                                                    .sender_id(processMessage.getFrom())
                                                                    .is_template(false)
                                                                    .body(sb.toString())
                                                                    .template_name("booking_done")
                                                                    .build());
                                                }
                                        );
                            }
                    );
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            saveContext= null;
            date=null;
        }
    }


}
