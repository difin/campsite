package org.difin.volcanic_getaways.reservation.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext context) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MMMM-dd");

        return LocalDate.parse(p.getText(), formatter);
    }
}