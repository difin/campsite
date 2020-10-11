package campsite.reservation.model;

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
