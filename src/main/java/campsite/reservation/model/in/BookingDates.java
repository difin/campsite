package campsite.reservation.model.in;

import campsite.reservation.validation.DateFormatValid;
import campsite.reservation.validation.DateWithinOneMonth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NotNull
public class BookingDates {

//    @DateTimeFormat(pattern = "yyyy-MMM-dd")
    @Future(message = "Arrival date must be no earlier then tomorrow")
//    @DateFormatValid(message = "Date format must be yyyy-MMM-dd")
    private LocalDate arrival;

//    @DateTimeFormat(pattern = "yyyy-MMM-dd")
    @Future(message = "Departure date cannot be in the past")
//    @DateFormatValid(message = "Date format must be yyyy-MMM-dd")
    @DateWithinOneMonth(message = "End date must be within one month")
    private LocalDate departure;
}
