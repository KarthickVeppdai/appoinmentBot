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
                                            saveContext = new UserContext("APPOINMENT", 0,
                                                    List.of(doctor, "DATE", "SLOT"), List.of(1, 0, 0), false, 1, processMessage);
                                            System.out.println("Docotor Slot completed.Next going for Date" + doctor);
                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                            messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("date"), "", false, List.of("")));
                                        } else {
                                            saveContext = new UserContext("APPOINMENT", 0,
                                                    List.of("DOCTOR", "DATE", "SLOT"), List.of(0, 0, 0), false, 0, processMessage);
                                            System.out.println("Ask Again for Doctor");
                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                            messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("repeat.doctor"), "", false, List.of("")));
                                        }
                                        break;
                                    case 1:
                                        date = openAI.getAppoinmentIntent().extractDate(processMessage.getBody(), LocalDate.now().toString());
                                        extracted_date = Optional.ofNullable(LocalDate.parse(date));
                                        extracted_date.filter(extracted_date -> (!extracted_date.isBefore(LocalDate.now()) && !extracted_date.isAfter(LocalDate.now().plusDays(7))))
                                                .ifPresentOrElse(
                                                        extracted_date -> {
                                                            saveContext = new UserContext("APPOINMENT", 0,
                                                                    List.of(userContext1.getSlots().get(0), extracted_date.toString(), "SLOT"), List.of(1, 1, 0), false, 2, processMessage);
                                                            System.out.println("Date Slot completed.Next going for slot" + date);
                                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                                            messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("timeslot"), "", false, List.of("")));

                                                        },
                                                        () -> {
                                                            saveContext = new UserContext("APPOINMENT", 0,
                                                                    List.of(userContext1.getSlots().get(0), "DATE", "SLOT"), List.of(1, 0, 0), false, 1, processMessage);
                                                            System.out.println("Ask Again for Date" + date);
                                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                                            messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("repeat.date"), "", false, List.of("")));

                                                        }
                                                );
                                        break;
                                    case 2:
                                        slot = openAI.getAppoinmentIntent().extractSlot(processMessage.getBody());
                                        slotTime = LocalTime.parse(slot);
                                        now = LocalTime.now();
                                        if (!(slotTime.getHour()==00)) {
                                            saveContext = new UserContext("APPOINMENT", 0,
                                                    List.of(userContext1.getSlots().get(0), userContext1.getSlots().get(1),slotTime.toString()), List.of(1, 1, 1), true, 2, processMessage);
                                            System.out.println("Slot Slot completed.Next going for Confirmation" + slotTime);
                                            messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getAppointmentConfirmation("confirmation",userContext1.getSlots().get(0), userContext1.getSlots().get(1),slotTime.toString()), "", false, List.of("")));
                                            redisService.saveData(processMessage.getFrom(), saveContext);

                                        } else {
                                            saveContext = new UserContext("APPOINMENT", 0,
                                                    List.of(userContext1.getSlots().get(0), userContext1.getSlots().get(1), "SLOT"), List.of(1, 1, 0), false, 2, processMessage);
                                            System.out.println("Ask Again for slot" + slotTime);
                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                            messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("repeat.timeslot"), "", false, List.of("")));
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
                                                    saveContext = new UserContext("WELCOME", 0,
                                                            List.of(""), List.of(0), false, 0, processMessage);
                                                    System.out.println("Booking Confirmed-----Going to Welcome");
                                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                                    //send 2 message one completed message and welcome as usual
                                                    messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("booking.done"), "", false, List.of("")));
                                                    //greeting need to be customized
                                                    messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("greeting"), "", false, List.of("")));

                                                },
                                                () -> {

                                                    saveContext = new UserContext("APPOINMENT", 0,
                                                            List.of("DOCTOR", "DATE", "SLOT"), List.of(0, 0, 0), false, 0, processMessage);
                                                    System.out.println("Booking failed going to Appoinment");
                                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                                    messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), textSupplyService.getMessage("booking.cancel"), "", false, List.of("")));
                                                    // You Cancelled previous appoinment try from first.
                                                    //Welocme Please Choose doctor Name Send Doctor Information. help and ask for appointmnt to channel
                                                    StringBuilder sb = new StringBuilder(textSupplyService.getMessage("appointment") + "\n");
                                                    for (String doctor : utilityConstants.docotorsList()) {
                                                        sb.append("➡\uFE0F").append(doctor).append("\n");
                                                    }
                                                    messageDispatcher.sendMessage(new MessageOutput(processMessage.getFrom(), sb.toString(), "", false, List.of("")));


                                                }
                                        );
                            }
                    );
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            date = null;
        }
    }


}
