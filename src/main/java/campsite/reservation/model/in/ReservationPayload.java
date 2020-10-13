package campsite.reservation.model.in;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReservationPayload {

    @NotNull(message = "{campsite.validation.null.name}")
    @NotEmpty(message = "{campsite.validation.empty.name}")
    @ApiModelProperty(position = 1)
    private String name;

    @NotNull(message = "{campsite.validation.null.email}")
    @NotEmpty(message = "{campsite.validation.empty.email}")
    @Email(message = "{campsite.validation.client.email.format}")
    @ApiModelProperty(position = 2)
    private String email;

    @Valid
    @ApiModelProperty(position = 3)
    private BookingDates bookingDates;
}
