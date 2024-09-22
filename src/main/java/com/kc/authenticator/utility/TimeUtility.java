package com.kc.authenticator.utility;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtility {

    public String getTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
                .plusMinutes(10)
                .format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getTime(Integer plusMinutes) {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
                .plusMinutes(plusMinutes)
                .format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
