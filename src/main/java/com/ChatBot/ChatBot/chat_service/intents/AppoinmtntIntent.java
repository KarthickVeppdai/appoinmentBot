package com.ChatBot.ChatBot.chat_service.intents;


import com.ChatBot.ChatBot.Util.UtilityConstants;
import com.ChatBot.ChatBot.chat_configuration.OpenAI;
import com.ChatBot.ChatBot.chat_service.mangers.IntentHandler;
import com.ChatBot.ChatBot.database.RedisService;
import com.ChatBot.ChatBot.models.ProcessMessage;
import com.ChatBot.ChatBot.models.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
                                            // send message for requesting date
                                        } else {
                                            saveContext = new UserContext("APPOINMENT", 0,
                                                    List.of("DOCTOR", "DATE", "SLOT"), List.of(0, 0, 0), false, 0, processMessage);
                                            System.out.println("Ask Again for Doctor");
                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                            // Send again for doctor
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
                                                            // send message for requesting Slot
                                                        },
                                                        () -> {
                                                            saveContext = new UserContext("APPOINMENT", 0,
                                                                    List.of("DOCTOR", "DATE", "SLOT"), List.of(1, 0, 0), false, 1, processMessage);
                                                            System.out.println("Ask Again for Date" + date);
                                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                                            // Send again for date
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
                                            redisService.saveData(processMessage.getFrom(), saveContext);

                                        } else {
                                            saveContext = new UserContext("APPOINMENT", 0,
                                                    List.of("DOCTOR", "DATE", "SLOT"), List.of(1, 1, 0), false, 2, processMessage);
                                            System.out.println("Ask Again for slot" + slotTime);
                                            redisService.saveData(processMessage.getFrom(), saveContext);
                                            // send message for requesting Slot
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
                                                },
                                                () -> {

                                                    saveContext = new UserContext("APPOINMENT", 0,
                                                            List.of("DOCTOR", "DATE", "SLOT"), List.of(0, 0, 0), false, 0, processMessage);
                                                    System.out.println("Booking failed going to Appoinment");
                                                    redisService.saveData(processMessage.getFrom(), saveContext);
                                                    // You Cancelled previous appoinment try from first.
                                                    //Welocme Please Choose doctor Name Send Doctor Information. help and ask for appointmnt to channel


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
