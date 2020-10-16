package org.difin.volcanic_getaways.reservation.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateConversionUtils {

    public static LocalDate stringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MMM-dd");
        return LocalDate.parse(date, formatter);
    }

    public static String dateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MMM-dd");
        return date.format(formatter);
    }
}
