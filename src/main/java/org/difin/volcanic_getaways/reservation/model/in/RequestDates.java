package org.difin.volcanic_getaways.reservation.model.in;

import org.difin.volcanic_getaways.reservation.utils.DateConversionUtils;
import org.difin.volcanic_getaways.reservation.validation.DateFormatValid;
import org.difin.volcanic_getaways.reservation.validation.DateWithinOneMonth;
import org.difin.volcanic_getaways.reservation.validation.DepartureAfterArrivalValid;
import org.difin.volcanic_getaways.reservation.validation.FutureReservationDate;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@NotNull
@NoArgsConstructor
@AllArgsConstructor
@DepartureAfterArrivalValid(message = "{volcanic_getaways.validation.departure.before.arrival}")
public class RequestDates {

    public RequestDates(LocalDate arrival, LocalDate departure) {
        this.arrival = DateConversionUtils.dateToString(arrival);
        this.departure = DateConversionUtils.dateToString(departure);
    }

    @ApiModelProperty(position = 1, example = "2020-Oct-10")
    @DateFormatValid(message = "{volcanic_getaways.validation.illegal.arrival.date.format}")
    @FutureReservationDate(message = "Arrival {volcanic_getaways.validation.date.cannot.be.in.the.past}")
    private String arrival;

    @ApiModelProperty(position = 2, example = "2020-Oct-10")
    @DateFormatValid(message = "{volcanic_getaways.validation.illegal.departure.date.format}")
    @FutureReservationDate(message = "Departure {volcanic_getaways.validation.date.cannot.be.in.the.past}")
    @DateWithinOneMonth(message = "{volcanic_getaways.validation.departure.date.too.far}")
    private String departure;

    public LocalDate getArrivalAsDate(){
        return DateConversionUtils.stringToDate(arrival);
    }

    public LocalDate getDepartureAsDate(){
        return DateConversionUtils.stringToDate(departure);
    }
}
