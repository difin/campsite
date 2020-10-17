package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.data.repository.ReservationRepository;
import org.difin.volcanic_getaways.reservation.exception.ReservationNotFoundException;
import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.service.reservation.CancellationServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancellationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private CancellationServiceImpl cancellationService;

    private final int id = 1;
    private final String name = "some name";
    private final String email = "some email";
    private final String bookingRef = "some booking reference";

    @DisplayName("When cancelling existing reservation then it gets cancelled successfully")
    @Test
    void cancellingExistingReservationTest(){

        BookingReferencePayload payload = new BookingReferencePayload(bookingRef);

        Reservation reservation = Reservation.builder()
                .id(id).name(name).email(email).bookingRef(bookingRef).reservedDates(Lists.emptyList()).build();

        when(reservationRepository.findByBookingRef(bookingRef)).thenReturn(reservation);

        boolean actual = cancellationService.cancelReservationBlocking(payload);

        assertTrue(actual);
    }

    @DisplayName("When attempting to cancel a reservation that doesn't exist then exception is thrown")
    @Test
    void attemptingToCancelNotExistentReservationTest(){

        BookingReferencePayload payload = new BookingReferencePayload(bookingRef);

        when(reservationRepository.findByBookingRef(bookingRef)).thenReturn(null);

        assertThrows(ReservationNotFoundException.class, () -> cancellationService.cancelReservationBlocking(payload));
    }
}