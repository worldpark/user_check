package com.check.user_check.converter.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LenientLocalTimeDeserializer extends JsonDeserializer<LocalTime> {

    private static final DateTimeFormatter HH_MM_SS = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String time = p.getText();
        if (time == null || time.isEmpty()) {
            return null;
        }

        try {
            return LocalTime.parse(time, HH_MM_SS);
        } catch (DateTimeParseException e) {
            LocalTime parsed = LocalTime.parse(time, HH_MM);
            return parsed.withSecond(0);
        }
    }
}
