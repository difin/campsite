package org.difin.volcanic_getaways.reservation.validation;

import org.difin.volcanic_getaways.reservation.model.request.BookingDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class BookingLengthLimitValidator implements ConstraintValidator<BookingLengthLimit, BookingDates> {

    public void initialize(BookingLengthLimit constraint) {
    }

    public boolean isValid(BookingDates bookingDates, ConstraintValidatorContext context) {

            LocalDate arrival = bookingDates.getArrival();
            LocalDate departure = bookingDates.getDeparture();

            return DAYS.between(arrival, departure) <= 3;
    }
}