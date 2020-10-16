package org.difin.volcanic_getaways.reservation.model.in;

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

    @NotNull(message = "{volcanic_getaways.validation.null.name}")
    @NotEmpty(message = "{volcanic_getaways.validation.empty.name}")
    @ApiModelProperty(position = 1)
    private String name;

    @NotNull(message = "{volcanic_getaways.validation.null.email}")
    @NotEmpty(message = "{volcanic_getaways.validation.empty.email}")
    @Email(message = "{volcanic_getaways.validation.client.email.format}")
    @ApiModelProperty(position = 2)
    private String email;

    @Valid
    @ApiModelProperty(position = 3)
    private BookingDates bookingDates;
}
