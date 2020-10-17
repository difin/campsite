package org.difin.volcanic_getaways.reservation.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(String errorMessage){
        super(errorMessage);
    }
}
