package org.difin.volcanic_getaways.reservation.exception;

import org.difin.volcanic_getaways.reservation.model.response.ErrorModel;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler  {

    @ExceptionHandler(value = {ReservationNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(ReservationNotFoundException e, WebRequest request) {

        ErrorModel errorModel = new ErrorModel(e);

        return new ResponseEntity(errorModel, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {RequestedRangeIsBookedException.class})
    protected ResponseEntity<Object> handleConflict(RequestedRangeIsBookedException e, WebRequest request) {

        ErrorModel errorModel = new ErrorModel(e);

        return new ResponseEntity(errorModel, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        ErrorModel errorModel = new ErrorModel(ex.getMessage().substring(0, ex.getMessage().indexOf("; nested exception is")));

        return new ResponseEntity(errorModel, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        ErrorModel errorModel = new ErrorModel();

        ex.getBindingResult().getFieldErrors()
                .stream()
                .forEach(e -> errorModel.addError(e.getDefaultMessage()));

        return new ResponseEntity(errorModel, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        ErrorModel errorModel = new ErrorModel();

        ex.getConstraintViolations()
                .stream()
                .forEach(e -> errorModel.addError(e.getMessage()));

        return new ResponseEntity(errorModel, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorModel errorModel = null;

        if ((ex.getCause() != null) && (ex.getCause().getCause() != null))
            errorModel = new ErrorModel(ex.getCause().getCause().getMessage());
        else
            errorModel = new ErrorModel(ex.getCause().getMessage());

        return new ResponseEntity(errorModel, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleOtherExceptions(RuntimeException e, WebRequest request) {

        ErrorModel errorModel = new ErrorModel(e);

        return new ResponseEntity(errorModel, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
