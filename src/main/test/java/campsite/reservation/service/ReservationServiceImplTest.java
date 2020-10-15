package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.entity.Reservation;
import campsite.reservation.data.entity.ReservedDate;
import campsite.reservation.data.repository.ReservationRepository;
import campsite.reservation.data.repository.ReservedDateRepository;
import campsite.reservation.exception.CampsiteException;
import campsite.reservation.model.in.BookingDates;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.service.reservation.ReservationServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservedDateRepository reservedDateRepository;

    @Mock
    private ManagedDatesFacade managedDatesFacade;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private final String arrival = "2020-Nov-01";
    private final String departure = "2020-Nov-04";
    private final String name = "some name";
    private final String email = "some email";
    private final String bookingRef = "some booking reference";

    private final ManagedDate nov01 = ManagedDate.builder().id(1).date( LocalDate.of(2020, 11, 1)).reservedDates(Lists.emptyList()).build();
    private final ManagedDate nov02 = ManagedDate.builder().id(2).date( LocalDate.of(2020, 11, 2)).reservedDates(Lists.emptyList()).build();
    private final ManagedDate nov03 = ManagedDate.builder().id(3).date( LocalDate.of(2020, 11, 3)).reservedDates(Lists.emptyList()).build();

    List<ManagedDate> availableDates = new ArrayList<>();

    @DisplayName("When making a reservation and requested dates are available then these dates are " +
            "stored in reserved_dates table and booking reference number is returned")
    @Test
    void allRequestedDatesAvailableTest(){

        availableDates.add(nov01);
        availableDates.add(nov02);
        availableDates.add(nov03);

        BookingDates bookingDates = new BookingDates(arrival, departure);
        ReservationPayload payload = ReservationPayload.builder().name(name).email(email).bookingDates(bookingDates).build();

        when(managedDatesFacade.getAvailableDatesBlocking(bookingDates)).thenReturn(availableDates);

        Reservation actual = reservationService.reserveInPresentTransaction(payload, Optional.of(bookingRef));

        ArgumentCaptor<ReservedDate> argumentCaptor = ArgumentCaptor.forClass(ReservedDate.class);
        verify(reservedDateRepository, times(3)).save(argumentCaptor.capture());

        List<ManagedDate> reservedDates = argumentCaptor.getAllValues()
                .stream().map(ReservedDate::getManagedDate).collect(Collectors.toList());

        assertTrue(reservedDates.containsAll(availableDates));

        assertEquals(bookingRef, actual.getBookingRef());
        assertEquals(name, actual.getName());
        assertEquals(email, actual.getEmail());
    }

    @DisplayName("When making a reservation and all requested dates are not available then CampsiteException is thrown")
    @Test
    void allRequestedDatesAreNotAvailableTest(){

        BookingDates bookingDates = new BookingDates(arrival, departure);
        ReservationPayload payload = ReservationPayload.builder().name(name).email(email).bookingDates(bookingDates).build();

        when(managedDatesFacade.getAvailableDatesBlocking(bookingDates)).thenReturn(availableDates);

        Assertions.assertThrows(CampsiteException.class, () ->
                reservationService.reserveInPresentTransaction(payload, Optional.of(bookingRef)));
    }

    @DisplayName("When making a reservation and only 2 out of 3 requested dates are available then CampsiteException is thrown")
    @Test
    void someRequestedDatesAreNotAvailableTest(){

        availableDates.add(nov01);
        availableDates.add(nov02);

        BookingDates bookingDates = new BookingDates(arrival, departure);
        ReservationPayload payload = ReservationPayload.builder().name(name).email(email).bookingDates(bookingDates).build();

        when(managedDatesFacade.getAvailableDatesBlocking(bookingDates)).thenReturn(availableDates);

        Assertions.assertThrows(CampsiteException.class, () ->
                reservationService.reserveInPresentTransaction(payload, Optional.of(bookingRef)));
    }
}