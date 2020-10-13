package campsite.reservation.service;

import campsite.reservation.data.entity.Reservation;
import campsite.reservation.data.repository.ReservationRepository;
import campsite.reservation.data.repository.ReservedDateRepository;
import campsite.reservation.model.ModelConverter;
import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.internal.CancellationStatus;
import campsite.reservation.validation.MethodParamValidator;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservedDateRepository reservedDateRepository;

    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private ReactiveExecutionService reactiveExecutionService;

    @Mock
    private ModelConverter modelConverter;

    @Mock
    private MethodParamValidator methodParamValidator;

    @InjectMocks
    ReservationServiceImpl reservationServiceImpl;

    @DisplayName("When cancelling existing reservation then it gets cancelled successfully")
    @Test
    void cancellingExistingReservation(){

        int id = 1;
        String name = "some name";
        String email = "some email";
        String bookingRef = "some booking reference";

        BookingReferencePayload payload = new BookingReferencePayload(bookingRef);

        Reservation reservation = Reservation.builder()
                .id(id).name(name).email(email).bookingRef(bookingRef).reservedDates(Lists.emptyList()).build();

        when(reservationRepository.findByBookingRef(bookingRef)).thenReturn(reservation);

        CancellationStatus actual = reservationServiceImpl.cancelInPresentTransaction(payload);

        assertEquals(CancellationStatus.SUCCESS, actual);
    }

    @DisplayName("When attempting to cancel a reservation that doesn't exist and error status is returned")
    @Test
    void cancellingAbsentReservation(){

        String bookingRef = "some booking reference";
        BookingReferencePayload payload = new BookingReferencePayload(bookingRef);

        when(reservationRepository.findByBookingRef(bookingRef)).thenReturn(null);

        CancellationStatus actual = reservationServiceImpl.cancelInPresentTransaction(payload);

        assertEquals(CancellationStatus.NOT_FOUND, actual);
    }
}