package org.difin.volcanic_getaways.reservation.validation;

import org.difin.volcanic_getaways.reservation.model.request.BookingDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import static java.time.temporal.ChronoUnit.DAYS;

public class BookingLengthLimitValidator implements ConstraintValidator<BookingLengthLimit, BookingDates> {

    public void initialize(BookingLengthLimit constraint) {
    }

    public boolean isValid(BookingDates bookingDates, ConstraintValidatorContext context) {

        try {
            LocalDate arrival = bookingDates.getArrivalAsDate();
            LocalDate departure = bookingDates.getDepartureAsDate();

            return DAYS.between(arrival, departure) <= 3;
        }
        catch (DateTimeParseException e) {
            // if date is not parsable, another more precise validation error will be shows to user
            return true;
        }
    }
}