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

        String errorMessage = exception.getMessage().trim();

        if (errorMessage.contains("default message"))
            errorMessage = errorMessage.substring(errorMessage.lastIndexOf("default message") + 15).trim();

        if (errorMessage.startsWith("[") && errorMessage.endsWith("]"))
            errorMessage = errorMessage.substring(1, errorMessage.length()-1);

        CampsiteErrorMessage campsiteErrorMessage = new CampsiteErrorMessage(errorMessage);
        return new ResponseEntity<>(campsiteErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
