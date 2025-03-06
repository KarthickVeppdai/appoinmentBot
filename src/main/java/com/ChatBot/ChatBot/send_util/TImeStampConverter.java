package com.ChatBot.ChatBot.send_util;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class TImeStampConverter {



    public Date timeStampConverter(String timstamp)
    {
        long milliseconds = Long.parseLong(timstamp) * 1000;
        return new Date(milliseconds);

    }

}
