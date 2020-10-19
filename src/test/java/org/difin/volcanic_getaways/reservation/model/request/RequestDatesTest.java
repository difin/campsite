package org.difin.volcanic_getaways.reservation.model.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RequestDatesTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("When request range is 3 days and dates are in future within 1 months than validation succeeds")
    @Test
    void validStayLengthTest(){

        RequestDates requestDates = new RequestDates(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));

        Set<ConstraintViolation<RequestDates>> violations = validator.validate(requestDates);

        assertTrue(violations.isEmpty());
    }

    @DisplayName("When arrival date is not in future then validation fails")
    @Test
    void arrivalDateNotInFutureTest(){

        RequestDates requestDates = new RequestDates(LocalDate.now(), LocalDate.now().plusDays(1));

        Set<ConstraintViolation<RequestDates>> violations = validator.validate(requestDates);

        assertTrue(violations.size() == 1);
        assertTrue(violations
                .stream()
                .findFirst()
                .get()
                .getMessage()
                .equals("Arrival {volcanic_getaways.validation.date.cannot.be.in.the.past}"));
    }

    @DisplayName("When departure date is not in future then validation fails")
    @Test
    void departureDateNotInFutureTest(){

        RequestDates requestDates = new RequestDates(LocalDate.now().plusDays(1), LocalDate.now());

        Set<ConstraintViolation<RequestDates>> violations = validator.validate(requestDates);

        assertTrue(violations.size() >= 1);
        assertTrue(violations
                .stream()
                .map(t -> t.getMessage())
                .filter(t -> t.equals("Departure {volcanic_getaways.validation.date.cannot.be.in.the.past}"))
                .count() == 1);
    }

    @DisplayName("When departure date is more than one month and one day from now then validation fails")
    @Test
    void departureDateTooFarTest(){

        RequestDates requestDates = new RequestDates(LocalDate.now().plusDays(1), LocalDate.now().plusMonths(1).plusDays(2));

        Set<ConstraintViolation<RequestDates>> violations = validator.validate(requestDates);

        assertTrue(violations.size() == 1);
        assertTrue(violations
                .stream()
                .findFirst()
                .get()
                .getMessage()
                .equals("{volcanic_getaways.validation.departure.date.too.far}"));
    }

    @DisplayName("When departure is same date as arrival then validation fails")
    @Test
    void departureSameDateAsArrivalTest(){

        RequestDates requestDates = new RequestDates(LocalDate.now().plusDays(1), LocalDate.now().plusDays(1));

        Set<ConstraintViolation<RequestDates>> violations = validator.validate(requestDates);

        assertTrue(violations.size() == 1);
        assertTrue(violations
                .stream()
                .findFirst()
                .get()
                .getMessage()
                .equals("{volcanic_getaways.validation.departure.before.arrival}"));
    }

    @DisplayName("When departure is before arrival then validation fails")
    @Test
    void departureBeforeArrivalTest(){

        RequestDates requestDates = new RequestDates(LocalDate.now().plusDays(1), LocalDate.now());

        Set<ConstraintViolation<RequestDates>> violations = validator.validate(requestDates);

        assertTrue(violations.size() >= 1);
        assertTrue(violations
                .stream()
                .map(t -> t.getMessage())
                .filter(t -> t.equals("{volcanic_getaways.validation.departure.before.arrival}"))
                .count() == 1);
    }
}