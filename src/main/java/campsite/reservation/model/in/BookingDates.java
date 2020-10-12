package campsite.reservation.model.in;

import campsite.reservation.validation.BookingLengthLimit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@BookingLengthLimit(message = "Illegal booking dates' range: campsite can be reserved for max 3 days")
public class BookingDates extends RequestDates{

    public BookingDates(LocalDate arrival, LocalDate departure) {
        super(arrival, departure);
    }

    public BookingDates(String arrival, String departure) {
        super(arrival, departure);
    }
}
