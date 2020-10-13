package campsite.reservation.model.in;

import campsite.reservation.validation.BookingLengthLimit;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Builder
@Setter
@Getter
@BookingLengthLimit(message = "{campsite.validation.booking.length.too.long}")
public class BookingDates extends RequestDates{

    public BookingDates(LocalDate arrival, LocalDate departure) {
        super(arrival, departure);
    }

    public BookingDates(String arrival, String departure) {
        super(arrival, departure);
    }
}
