package campsite.reservation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FutureReservationDateValidator implements ConstraintValidator<FutureReservationDate, String> {
    public void initialize(FutureReservationDate constraint) {
    }

    public boolean isValid(String date, ConstraintValidatorContext context) {

        try{
            return stringToDate(date).isAfter(LocalDate.now().plusDays(1));
        } catch (DateTimeParseException e) {
            // if date is not parsable, another more precise validation error will be shows to user
            return true;
        }
    }

    private LocalDate stringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        return LocalDate.parse(date, formatter);
    }
}
