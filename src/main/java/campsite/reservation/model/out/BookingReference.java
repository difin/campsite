package campsite.reservation.model.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
public class BookingReference {

    @Getter
    @Setter
    private UUID bookingReference;
}
