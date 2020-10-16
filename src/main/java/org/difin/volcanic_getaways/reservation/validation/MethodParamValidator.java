package org.difin.volcanic_getaways.reservation.validation;

import org.difin.volcanic_getaways.reservation.model.in.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.in.RequestDates;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
@Validated
public class MethodParamValidator {

    public RequestDates validateRequestDates(@Valid RequestDates requestDates){
        return requestDates;
    }

    public BookingReferencePayload validateBookingReference(@Valid BookingReferencePayload bookingReference) {
        return bookingReference;
    }
}
