package campsite.reservation.model.in;

import campsite.reservation.validation.DateFormatValid;
import campsite.reservation.validation.DateWithinOneMonth;
import campsite.reservation.validation.FutureReservationDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@NotNull
@NoArgsConstructor
public class BookingDates {

    public BookingDates(LocalDate arrival,  LocalDate departure) {
        this.arrival = dateToString(arrival);
        this.departure = dateToString(departure);
    }

    public BookingDates(String arrival,
                        String departure) {
        this.arrival = arrival;
        this.departure = departure;
    }

    @DateFormatValid(message = "Arrival date format must be yyyy-MMM-dd")
    @FutureReservationDate(message = "Arrival date must be no earlier then tomorrow")
    private String arrival;

    @DateFormatValid(message = "Departure date format must be yyyy-MMM-dd")
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        return LocalDate.parse(date, formatter);
    }

    private String dateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
        return date.format(formatter);
    }
}
