package campsite.reservation.model.in;

import campsite.reservation.validation.DateFormatValid;
import campsite.reservation.validation.DateWithinOneMonth;
import campsite.reservation.validation.DepartureAfterArrivalValid;
import campsite.reservation.validation.FutureReservationDate;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Setter
@Getter
@NotNull
@NoArgsConstructor
@AllArgsConstructor
@DepartureAfterArrivalValid(message = "{campsite.validation.departure.before.arrival}")
public class RequestDates {

    public RequestDates(LocalDate arrival, LocalDate departure) {
        this.arrival = dateToString(arrival);
        this.departure = dateToString(departure);
    }

    @ApiModelProperty(position = 1, example = "2020-Oct-10")
    @DateFormatValid(message = "{campsite.validation.illegal.arrival.date.format}")
    @FutureReservationDate(message = "Arrival {campsite.validation.date.cannot.be.in.the.past}")
    private String arrival;

    @ApiModelProperty(position = 2, example = "2020-Oct-10")
    @DateFormatValid(message = "{campsite.validation.illegal.departure.date.format}")
    @FutureReservationDate(message = "Departure {campsite.validation.date.cannot.be.in.the.past}")
    @DateWithinOneMonth(message = "{campsite.validation.departure.date.too.far}")
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
