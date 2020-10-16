package org.difin.volcanic_getaways.reservation.model.request;

import org.difin.volcanic_getaways.reservation.validation.BookingLengthLimit;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Builder
@Setter
@Getter
@BookingLengthLimit(message = "{volcanic_getaways.validation.booking.length.too.long}")
@ApiModel(description = "allowed dates range = 3 days max")
public class BookingDates extends RequestDates{

    public BookingDates(LocalDate arrival, LocalDate departure) {
        super(arrival, departure);
    }

    public BookingDates(String arrival, String departure) {
        super(arrival, departure);
    }
}
