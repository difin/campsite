package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.exception.ReservationNotFoundException;
import org.difin.volcanic_getaways.reservation.model.ModelConverter;
import org.difin.volcanic_getaways.reservation.model.request.BookingDates;
import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.service.common.ReactiveExecutionService;
import org.difin.volcanic_getaways.reservation.service.reservation.CancellationService;
import org.difin.volcanic_getaways.reservation.service.reservation.ReservationService;
import org.difin.volcanic_getaways.reservation.service.reservation.UpdateServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateServiceImplTest {

    @Mock
    private ReactiveExecutionService reactiveExecutionService;

    @Mock
    private ModelConverter modelConverter;

    @Mock
    private CancellationService cancellationService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private UpdateServiceImpl updateService;

    private final int reservationId = 1;
    private final String name = "some name";
    private final String email = "some email";
    private final String bookingRef = "some booking reference";

    private final String arrival = "2020-November-01";
    private final String departure = "2020-November-04";

    private final BookingDates bookingDates = new BookingDates(arrival, departure);
    private final ReservationPayload reservationPayload = ReservationPayload.builder().name(name).email(email).bookingDates(bookingDates).build();
    private final BookingReferencePayload bookingReferencePayload = new BookingReferencePayload(bookingRef);

    @DisplayName("When updating existing reservation then it gets updated successfully")
    @Test
    void updatingExistingReservationTest(){

        when(cancellationService.cancelReservationBlocking(bookingReferencePayload)).thenReturn(true);

        assertTrue(updateService.updateReservationBlocking(bookingReferencePayload, reservationPayload));
    }

    @DisplayName("When trying to update existing reservation, but it is not found then ReservationNotFoundException is thrown")
    @Test
    void attemptingToUpdateNotExistentReservationTest(){

        when(cancellationService.cancelReservationBlocking(bookingReferencePayload)).thenThrow(ReservationNotFoundException.class);

        assertThrows(ReservationNotFoundException.class,
                () -> updateService.updateReservationBlocking(bookingReferencePayload, reservationPayload));
    }
}