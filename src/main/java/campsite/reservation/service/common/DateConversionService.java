package campsite.reservation.service.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateConversionService {

    public static LocalDate stringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MMM-dd", Locale.ENGLISH);
        return LocalDate.parse(date, formatter);
    }

    public static String dateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MMM-dd", Locale.ENGLISH);
        return date.format(formatter);
    }
}
