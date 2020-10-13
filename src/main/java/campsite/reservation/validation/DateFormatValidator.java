package campsite.reservation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class DateFormatValidator implements ConstraintValidator<DateFormatValid, String> {

    public void initialize(DateFormatValid constraint) {
    }

    public boolean isValid(String date, ConstraintValidatorContext context) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MMM-dd", Locale.ENGLISH);
        boolean isValid = true;

        try {
            LocalDate.parse(date, formatter);
        }
        catch (DateTimeParseException e) {
            isValid = false;
        }

        return isValid;
    }
}
