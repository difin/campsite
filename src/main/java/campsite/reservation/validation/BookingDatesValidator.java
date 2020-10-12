package campsite.reservation.validation;

import campsite.reservation.model.in.BookingDates;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Component
@Validated
public class BookingDatesValidator {

    public BookingDates validateDates(@Valid BookingDates bookingDates){
        return bookingDates;
    }
}
