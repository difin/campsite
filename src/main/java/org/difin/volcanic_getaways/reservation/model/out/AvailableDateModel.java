package org.difin.volcanic_getaways.reservation.model.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateModel {

    @Getter
    @Setter
    private LocalDate availableDate;
}
