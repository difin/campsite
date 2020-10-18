package org.difin.volcanic_getaways.reservation.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.difin.volcanic_getaways.reservation.utils.LocalDateDeserializer;
import org.difin.volcanic_getaways.reservation.validation.DateWithinOneMonth;
import org.difin.volcanic_getaways.reservation.validation.DepartureAfterArrival;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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

    @ApiModelProperty(position = 1, example = "2020-November-07")
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(pattern="uuuu-MMMM-dd")
    @Future(message = "Arrival {volcanic_getaways.validation.date.cannot.be.in.the.past}")
    private LocalDate arrival;

    @ApiModelProperty(position = 2, example = "2020-November-10")
    @JsonDeserialize(using= LocalDateDeserializer.class)
    @JsonFormat(pattern="uuuu-MMMM-dd")
    @DateWithinOneMonth(message = "{volcanic_getaways.validation.departure.date.too.far}")
    @Future(message = "Departure {volcanic_getaways.validation.date.cannot.be.in.the.past}")
    private LocalDate departure;
}
