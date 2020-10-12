package campsite.reservation.validation;

import campsite.reservation.model.in.RequestDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DepartureAfterArrivalValidator implements ConstraintValidator<DepartureAfterArrivalValid, RequestDates> {

    public void initialize(DepartureAfterArrivalValid constraint) {
    }

    public boolean isValid(RequestDates requestDates, ConstraintValidatorContext context) {

        try {
            LocalDate arrival = requestDates.getArrivalAsDate();
            LocalDate departure = requestDates.getDepartureAsDate();

            return departure.isAfter(arrival);
        }
        catch (DateTimeParseException e) {
            // if date is not parsable, another more precise validation error will be shows to user
            return true;
        }
    }
}