package campsite.reservation.validation;

import campsite.reservation.exception.CampsiteException;
import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.RequestDates;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
@Validated
public class MethodParamValidator {

    public RequestDates validateRequestDates(@Valid RequestDates requestDates){
        return requestDates;
    }

    public BookingReferencePayload validateBookingReference(@Valid BookingReferencePayload bookingReference) {
        return bookingReference;
    }

    public void validateCampsiteAvailability(RequestDates requestDates, int availableDaysCount){

        if (DAYS.between(requestDates.getArrivalAsDate(), requestDates.getDepartureAsDate()) > availableDaysCount){
            throw new CampsiteException("Reservation couldn't proceed because campsite is at full capacity at one or more days, " +
                    "please choose other dates");
        }
    }
}
