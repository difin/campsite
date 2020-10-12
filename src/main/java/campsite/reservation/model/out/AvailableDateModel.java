package campsite.reservation.model.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
public class AvailableDateModel {

    @Getter
    @Setter
    LocalDate availableDate;
}
