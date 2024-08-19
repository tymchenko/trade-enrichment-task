package com.verygoodbank.tes.service.validation;

import java.text.ParsePosition;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;


public class DateValidator {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd")
            .withResolverStyle(ResolverStyle.STRICT);

    public static Boolean notValidDate(String date) {
        ParsePosition pos = new ParsePosition(0);
        DATE_FORMATTER.parseUnresolved(date, pos);
        return pos.getErrorIndex() != -1;
    }
}
