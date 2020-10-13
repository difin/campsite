package campsite.reservation.model.in;

import campsite.reservation.validation.DateFormatValid;
import campsite.reservation.validation.DateWithinOneMonth;
import campsite.reservation.validation.DepartureAfterArrivalValid;
import campsite.reservation.validation.FutureReservationDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Setter
@Getter
@NotNull
@NoArgsConstructor
@AllArgsConstructor
@DepartureAfterArrivalValid(message = "Illegal booking dates' range: campsite can be reserved for max 3 days")
public class RequestDates {

    public RequestDates(LocalDate arrival, LocalDate departure) {
        this.arrival = dateToString(arrival);
        this.departure = dateToString(departure);
    }

    @DateFormatValid(message = "Arrival date format must be uuuu-MMM-dd")
    @FutureReservationDate(message = "Arrival date must be no earlier then tomorrow")
    private String arrival;

    @DateFormatValid(message = "Departure date format must be uuuu-MMM-dd")
    @FutureReservationDate(message = "Departure date cannot be in the past")
    @DateWithinOneMonth(message = "End date must be within one month")
    private String departure;

    public LocalDate getArrivalAsDate(){
        return stringToDate(arrival);
    }

    public LocalDate getDepartureAsDate(){
        return stringToDate(departure);
    }

    private LocalDate stringToDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MMM-dd", Locale.ENGLISH);
        return LocalDate.parse(date, formatter);
    }

    private String dateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MMM-dd", Locale.ENGLISH);
        return date.format(formatter);
    }
}
