package org.difin.volcanic_getaways.reservation.exception;

import org.difin.volcanic_getaways.reservation.model.response.ErrorModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler  {

    @ExceptionHandler(value = {ReservationNotFoundException.class})
    protected ResponseEntity<Object> handleReservationNotFoundException(ReservationNotFoundException e, WebRequest request) {

        ErrorModel errorModel = new ErrorModel(e);

        return new ResponseEntity(errorModel, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RequestedRangeIsBookedException.class})
    protected ResponseEntity<Object> handleRequestedRangeIsBookedException(RequestedRangeIsBookedException e, WebRequest request) {

        ErrorModel errorModel = new ErrorModel(e);

        return new ResponseEntity(errorModel, new HttpHeaders(), HttpStatus.CONFLICT);
    }

//    @ExceptionHandler
//    public ResponseEntity<VolcanicGetawaysErrorMessage> handleAllExceptions(Exception exception,
//                                                                            WebRequest webRequest) {
//
//        String errorMessage = exception.getMessage().trim();
//
//        if (errorMessage.contains("default message"))
//            errorMessage = errorMessage.substring(errorMessage.lastIndexOf("default message") + 15).trim();
//
//        if (errorMessage.startsWith("[") && errorMessage.endsWith("]"))
//            errorMessage = errorMessage.substring(1, errorMessage.length()-1);
//
//        if (errorMessage.endsWith("]") && !errorMessage.contains("["))
//            errorMessage = errorMessage.substring(0, errorMessage.length()-1);
//
//        VolcanicGetawaysErrorMessage volcanicGetawaysErrorMessage = new VolcanicGetawaysErrorMessage(errorMessage);
//        return new ResponseEntity<>(volcanicGetawaysErrorMessage, HttpStatus.OK);
//    }

}
