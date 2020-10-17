package org.difin.volcanic_getaways.reservation.exception;

public class RequestedRangeIsBookedException extends RuntimeException {

    public RequestedRangeIsBookedException(String errorMessage){
        super(errorMessage);
    }
}
