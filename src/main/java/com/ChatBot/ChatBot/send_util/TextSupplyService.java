package com.ChatBot.ChatBot.send_util;

import com.ChatBot.ChatBot.chat_configuration.TextConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class TextSupplyService {


    @Autowired
    private TextConfig textConfig;

    public String getMessage(String code) {
        //LocaleContextHolder.getLocale()
        return textConfig.messageSource().getMessage(code, null, Locale.ENGLISH);
    }

    public String getAppointmentConfirmation(String code,String doctorName,String date, String time) {
        return textConfig.messageSource().getMessage("appointment.confirmation", new Object[]{doctorName,date, time},Locale.ENGLISH);
    }
}
