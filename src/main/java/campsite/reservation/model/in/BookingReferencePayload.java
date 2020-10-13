package campsite.reservation.model.in;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@Setter
public class BookingReferencePayload {

    @NotNull(message = "campsite.validation.booking.reference.null")
    @NotEmpty(message = "campsite.validation.booking.reference.empty")
    @Size(min = 36, max = 36)
    @ApiModelProperty(position = 1, notes = "36 characters UUID value")
    private String bookingReference;
}
