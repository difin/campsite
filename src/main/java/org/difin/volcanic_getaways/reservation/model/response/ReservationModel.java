package org.difin.volcanic_getaways.reservation.model.response;

import lombok.*;

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
    private List<String> reservedDates;
}
