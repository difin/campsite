package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.entity.Reservation;
import campsite.reservation.data.entity.ReservedDate;
import campsite.reservation.data.repository.ReservationRepository;
import campsite.reservation.data.repository.ReservedDateRepository;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.model.out.ModelConverter;
import campsite.reservation.validation.BookingDatesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservedDateRepository reservedDateRepository;
    private final ModelConverter modelConverter;
    private final AvailabilityService availabilityService;
    private final BookingDatesValidator bookingDatesValidator;
    private final ReactiveExecutionService reactiveExecutionService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ReservedDateRepository reservedDateRepository,
                                  AvailabilityService availabilityService,
                                  ReactiveExecutionService reactiveExecutionService,
                                  ModelConverter modelConverter,
                                  BookingDatesValidator bookingDatesValidator){
        this.reservationRepository = reservationRepository;
        this.modelConverter = modelConverter;
        this.availabilityService = availabilityService;
        this.reservedDateRepository = reservedDateRepository;
        this.bookingDatesValidator = bookingDatesValidator;
        this.reactiveExecutionService = reactiveExecutionService;
    }

    public Mono<BookingReference> reserve(ReservationPayload payload) {

        return reactiveExecutionService.execTransaction(() ->
            {
                List<ManagedDate> availableDates = availabilityService.getAvailableDatesBlocking(payload.getBookingDates());

                bookingDatesValidator.validateCampsiteAvailability(payload.getBookingDates(), availableDates.size());

                Reservation reservation = new Reservation();

                reservation.setName(payload.getName());
                reservation.setEmail(payload.getEmail());
                reservation.setBookingRef(UUID.randomUUID().toString());

                reservationRepository.save(reservation);

                availableDates
                    .forEach(managedDate -> {
                            ReservedDate reservedDate = new ReservedDate();
                            reservedDate.setManagedDate(managedDate);
                            reservedDate.setReservationId(reservation);

                            reservedDateRepository.save(reservedDate);
                        }
                    );

                return reservation;
            })
        .map(modelConverter::reservationEntityToDTO);
    }

    public Mono<ActionResult> cancelReservation(String bookingReference) {
        return reactiveExecutionService.execTransaction(() ->
        {
            AtomicReference<ActionResult> actionResult = new AtomicReference<>();

            Optional<Reservation> reservation =
                Optional.ofNullable(reservationRepository
                    .findByBookingRef(bookingReference));

            reservation
                .ifPresentOrElse(
                    (r) -> {
                        r.getReservedDates()
                            .forEach(t -> reservedDateRepository.deleteById(t.getId()));

                        reservationRepository.deleteById(reservation.get().getId());

                        actionResult.set(new ActionResult("Reservation was deleted successfully"));
                    },
                    () -> {
                        actionResult.set(new ActionResult("Reservation couldn't be deleted because it doesn't exist"));
                    }
                );

            return actionResult.get();
        });
    }
}
