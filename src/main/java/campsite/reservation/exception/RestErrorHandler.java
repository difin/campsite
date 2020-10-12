package campsite.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler
    public ResponseEntity<CampsiteErrorMessage> handleAllExceptions(Exception exception,
                                                                    WebRequest webRequest) {
        CampsiteErrorMessage campsiteErrorMessage = new CampsiteErrorMessage(exception.getMessage());
        return new ResponseEntity<>(campsiteErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
