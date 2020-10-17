package org.difin.volcanic_getaways.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReservationNotFoundException extends RuntimeException {

    private String message;
}
