package campsite.reservation.model.in;

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
    private String name;

    @NotNull(message = "{campsite.validation.null.email}")
    @NotEmpty(message = "{campsite.validation.empty.email}")
    @Email(message = "{campsite.validation.client.email.format}")
    private String email;

    @Valid
    private BookingDates bookingDates;
}
