package campsite.reservation.model.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class BookingReference {

    @Getter
    @Setter
    private String bookingReference;
}
