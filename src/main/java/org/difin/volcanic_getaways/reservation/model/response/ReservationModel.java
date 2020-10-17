package org.difin.volcanic_getaways.reservation.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReservationModel {

    private int id;
    private String name;
    private String email;
    private String bookingRef;

    @JsonFormat(pattern="uuuu-MMMM-dd")
    private List<LocalDate> reservedDates;
}
