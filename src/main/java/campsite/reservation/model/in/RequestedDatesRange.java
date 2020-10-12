package campsite.reservation.model.in;

import campsite.reservation.validation.DateWithinOneMonth;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NotNull
public class RequestedDatesRange {

    @Future(message = "Start date must be no earlier then tomorrow")
    LocalDate startDate;

    @Future(message = "End date cannot be in the past")
    @DateWithinOneMonth(message = "End date must be within one month")
    LocalDate endDate;
}
