package org.difin.volcanic_getaways.reservation.model.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingDatesTest {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("When booking range is 3 days and dates are in future within 1 months than validation succeeds")
    @Test
    void validStayLengthTest(){

        BookingDates bookingDates = new BookingDates(LocalDate.now().plusDays(1), LocalDate.now().plusDays(4));

        Set<ConstraintViolation<RequestDates>> violations = validator.validate(bookingDates);

        assertTrue(violations.isEmpty());
    }

    @DisplayName("When booking range is more than 3 days than validation fails")
    @Test
    void tooLongStayTest(){

        BookingDates bookingDates = new BookingDates(LocalDate.now().plusDays(1), LocalDate.now().plusDays(5));

        Set<ConstraintViolation<RequestDates>> violations = validator.validate(bookingDates);

        assertTrue(violations.size() == 1);
        assertTrue(violations
                .stream()
                .findFirst()
                .get().getMessage()
                .equals("{volcanic_getaways.validation.booking.length.too.long}"));
    }
}