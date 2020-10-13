package campsite.reservation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateWithinOneMonthValidator implements ConstraintValidator<DateWithinOneMonth, String> {
    public void initialize(DateWithinOneMonth constraint) {
    }

    public boolean isValid(String date, ConstraintValidatorContext context) {

        try{
            return stringToDate(date).isBefore(LocalDate.now().plusMonths(1));
        } catch (DateTimeParseException e) {
            // if date is not parsable, another more precise validation error will be shows to user
            return true;
        }
    }

    private LocalDate stringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MMM-dd", Locale.ENGLISH);
        return LocalDate.parse(date, formatter);
    }
}
