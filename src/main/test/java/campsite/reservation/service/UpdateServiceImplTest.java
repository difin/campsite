package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.entity.Reservation;
import campsite.reservation.model.ModelConverter;
import campsite.reservation.model.in.BookingDates;
import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.internal.CancellationStatus;
import campsite.reservation.model.internal.UpdateStatus;
import campsite.reservation.service.common.ReactiveExecutionService;
import campsite.reservation.service.reservation.CancellationService;
import campsite.reservation.service.reservation.ReservationService;
import campsite.reservation.service.reservation.UpdateServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
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

    private int reservationId = 1;
    private String name = "some name";
    private String email = "some email";
    private String bookingRef = "some booking reference";

    private final ManagedDate nov01 = ManagedDate.builder().id(1).date( LocalDate.of(2020, 11, 1)).reservedDates(Lists.emptyList()).build();
    private final ManagedDate nov02 = ManagedDate.builder().id(2).date( LocalDate.of(2020, 11, 2)).reservedDates(Lists.emptyList()).build();
    private final ManagedDate nov03 = ManagedDate.builder().id(3).date( LocalDate.of(2020, 11, 3)).reservedDates(Lists.emptyList()).build();

    List<ManagedDate> availableDates = new ArrayList<>();

    private final String arrival = "2020-Nov-01";
    private final String departure = "2020-Nov-04";

    private BookingDates bookingDates = new BookingDates(arrival, departure);

    @DisplayName("When updating existing reservation then it gets updated successfully")
    @Test
    void updatingExistingReservation(){

        availableDates.add(nov01);
        availableDates.add(nov02);
        availableDates.add(nov03);

        ReservationPayload reservationPayload = ReservationPayload.builder().name(name).email(email).bookingDates(bookingDates).build();
        BookingReferencePayload bookingReferencePayload = new BookingReferencePayload(bookingRef);

        Reservation reservation = Reservation.builder()
                .id(reservationId).name(name).email(email).bookingRef(bookingRef).reservedDates(Lists.emptyList()).build();

        when(cancellationService.cancelInPresentTransaction(bookingReferencePayload)).thenReturn(CancellationStatus.SUCCESS);

        UpdateStatus actual = updateService.updateReservationInPresentTransaction(bookingReferencePayload, reservationPayload);

        assertEquals(UpdateStatus.SUCCESS, actual);
    }

    @DisplayName("When trying to update existing reservation, but it is not found then getting status not found")
    @Test
    void attemptingToUpdateNotExistentReservation(){

        availableDates.add(nov01);
        availableDates.add(nov02);
        availableDates.add(nov03);

        ReservationPayload reservationPayload = ReservationPayload.builder().name(name).email(email).bookingDates(bookingDates).build();
        BookingReferencePayload bookingReferencePayload = new BookingReferencePayload(bookingRef);

        Reservation reservation = Reservation.builder()
                .id(reservationId).name(name).email(email).bookingRef(bookingRef).reservedDates(Lists.emptyList()).build();

        when(cancellationService.cancelInPresentTransaction(bookingReferencePayload)).thenReturn(CancellationStatus.NOT_FOUND);

        UpdateStatus actual = updateService.updateReservationInPresentTransaction(bookingReferencePayload, reservationPayload);

        assertEquals(UpdateStatus.NOT_FOUND, actual);
    }
}