package org.difin.volcanic_getaways.reservation.validation;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
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
