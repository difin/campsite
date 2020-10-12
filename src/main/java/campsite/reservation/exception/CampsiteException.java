package campsite.reservation.exception;

public class CampsiteException extends RuntimeException {

    public CampsiteException(String errorMessage){
        super(errorMessage);
    }
}
