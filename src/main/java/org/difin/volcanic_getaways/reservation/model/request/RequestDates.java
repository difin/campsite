package org.difin.volcanic_getaways.reservation.model.request;

import org.difin.volcanic_getaways.reservation.validation.DateWithinOneMonth;
import org.difin.volcanic_getaways.reservation.validation.DepartureAfterArrival;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@NotNull
@NoArgsConstructor
@AllArgsConstructor
@DepartureAfterArrival(message = "{volcanic_getaways.validation.departure.before.arrival}")
public class RequestDates {

    @ApiModelProperty(position = 1, example = "2020-Oct-10")
    @DateTimeFormat(pattern = "uuuu-MMM-dd")
    @Future(message = "Arrival {volcanic_getaways.validation.date.cannot.be.in.the.past}")
    private LocalDate arrival;

    @ApiModelProperty(position = 2, example = "2020-Oct-10")
    @DateTimeFormat(pattern = "uuuu-MMM-dd")
    @DateWithinOneMonth(message = "{volcanic_getaways.validation.departure.date.too.far}")
    @Future(message = "Departure {volcanic_getaways.validation.date.cannot.be.in.the.past}")
    private LocalDate departure;

    public LocalDate getArrivalAsDate(){
        return arrival;
    }

    public LocalDate getDepartureAsDate(){
        return departure;
    }
}
