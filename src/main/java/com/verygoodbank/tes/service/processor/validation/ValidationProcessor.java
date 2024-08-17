package com.verygoodbank.tes.service.processor.validation;

import com.verygoodbank.tes.service.processor.Processor;
import com.verygoodbank.tes.web.model.TradeData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidationProcessor implements Processor<Boolean, TradeData> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public Boolean process(TradeData product) {
        try {
            LocalDate.parse(product.date(), DATE_FORMATTER);
            return true; // If parsing succeeds, the format is correct
        } catch (DateTimeParseException e) {
            return false; // If parsing fails, the format is incorrect
        }
    }
}
