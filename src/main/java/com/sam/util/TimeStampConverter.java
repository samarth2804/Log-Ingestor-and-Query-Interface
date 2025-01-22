package com.sam.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class TimeStampConverter {

    public ZonedDateTime convert(Date date) {
        return (date == null) ? null : date.toInstant().atZone(ZoneOffset.UTC);
    }

    public Date convert(ZonedDateTime zonedDateTime) {
        return (zonedDateTime == null) ? null : Date.from(zonedDateTime.toInstant());
    }

}