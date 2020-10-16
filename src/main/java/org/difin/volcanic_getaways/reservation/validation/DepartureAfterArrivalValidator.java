package org.difin.volcanic_getaways.reservation.validation;

import org.difin.volcanic_getaways.reservation.model.request.RequestDates;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DepartureAfterArrivalValidator implements ConstraintValidator<DepartureAfterArrival, RequestDates> {

    public void initialize(DepartureAfterArrival constraint) {
    }

    public boolean isValid(RequestDates requestDates, ConstraintValidatorContext context) {

        LocalDate arrival = requestDates.getArrival();
        LocalDate departure = requestDates.getDeparture();

        return departure.isAfter(arrival);
    }
}