package org.difin.volcanic_getaways.reservation.validation;

import org.difin.volcanic_getaways.reservation.exception.VolcanicGetawaysException;
import org.difin.volcanic_getaways.reservation.model.in.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.in.RequestDates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Locale;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@Validated
public class MethodParamValidator {

    @Autowired
    MessageSource messageSource;

    public RequestDates validateRequestDates(@Valid RequestDates requestDates){
        return requestDates;
    }

    public BookingReferencePayload validateBookingReference(@Valid BookingReferencePayload bookingReference) {
        return bookingReference;
    }

    public void validateSiteAvailability(RequestDates requestDates, int availableDaysCount){

        if (DAYS.between(requestDates.getArrivalAsDate(), requestDates.getDepartureAsDate()) > availableDaysCount){

            throw new VolcanicGetawaysException(
                    messageSource.getMessage("volcanic_getaways.exception.reservation.full.capacity", null, null, Locale.getDefault())
            );
        }
    }
}
