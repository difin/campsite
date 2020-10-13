package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.entity.Reservation;
import campsite.reservation.data.entity.ReservedDate;
import campsite.reservation.data.repository.ReservationRepository;
import campsite.reservation.data.repository.ReservedDateRepository;
import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.internal.CancellationStatus;
import campsite.reservation.model.internal.UpdateStatus;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.model.ModelConverter;
import campsite.reservation.validation.MethodParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private ReservedDateRepository reservedDateRepository;
    private AvailabilityService availabilityService;
    private ReactiveExecutionService reactiveExecutionService;
    private ModelConverter modelConverter;
    private MethodParamValidator methodParamValidator;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ReservedDateRepository reservedDateRepository,
                                  AvailabilityService availabilityService,
                                  ReactiveExecutionService reactiveExecutionService,
                                  ModelConverter modelConverter,
                                  MethodParamValidator methodParamValidator){
        this.reservationRepository = reservationRepository;
        this.modelConverter = modelConverter;
        this.availabilityService = availabilityService;
        this.reservedDateRepository = reservedDateRepository;
        this.methodParamValidator = methodParamValidator;
        this.reactiveExecutionService = reactiveExecutionService;
    }

    public Mono<BookingReference> reserve(ReservationPayload payload) {

        return reactiveExecutionService.execTransaction(() ->
                reserveInPresentTransaction(payload, Optional.empty()))
                .map(modelConverter::reservationEntityToDTO);
    }

    public Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload) {
        return reactiveExecutionService.execTransaction(() ->
                cancelInPresentTransaction(bookingReferencePayload))
                .map(modelConverter::cancellationStatusToDTO);
    }

    public Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload) {
        return reactiveExecutionService.execTransaction(() ->
                updateReservationInPresentTransaction(bookingReferencePayload, payload))
                .map(modelConverter::updateStatusToDTO);
    }

    protected Reservation reserveInPresentTransaction(ReservationPayload payload, Optional<String> bookingRef){

        List<ManagedDate> availableDates = availabilityService.getAvailableDatesEagerLocking(payload.getBookingDates());

        methodParamValidator.validateCampsiteAvailability(payload.getBookingDates(), availableDates.size());

        Reservation reservation = new Reservation();

        reservation.setName(payload.getName());
        reservation.setEmail(payload.getEmail());

        bookingRef.ifPresentOrElse(
                reservation::setBookingRef,
                () -> reservation.setBookingRef(UUID.randomUUID().toString())
        );

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
    }

    protected CancellationStatus cancelInPresentTransaction(BookingReferencePayload bookingReferencePayload) {

        AtomicReference<CancellationStatus> cancellationStatus = new AtomicReference<>();

        Optional<Reservation> reservation =
                Optional.ofNullable(reservationRepository
                        .findByBookingRef(bookingReferencePayload.getBookingReference()));

        reservation
            .ifPresentOrElse(
                (r) -> {
                    r.getReservedDates()
                        .forEach(t -> reservedDateRepository.deleteById(t.getId()));

                    reservationRepository.deleteById(r.getId());

                    cancellationStatus.set(CancellationStatus.SUCCESS);
                },
                () -> cancellationStatus.set(CancellationStatus.NOT_FOUND)
            );

        return cancellationStatus.get();
    }

    protected UpdateStatus updateReservationInPresentTransaction(BookingReferencePayload bookingReferencePayload,
                                                              ReservationPayload payload) {

        CancellationStatus cancellationStatus = cancelInPresentTransaction(bookingReferencePayload);
        UpdateStatus updateStatus = null;

        switch (cancellationStatus) {
            case NOT_FOUND:
                updateStatus =  UpdateStatus.NOT_FOUND;
                break;
            case SUCCESS:
                reserveInPresentTransaction(payload, Optional.of(bookingReferencePayload.getBookingReference()));
                updateStatus = UpdateStatus.SUCCESS;
                break;
        }

        return updateStatus;
    }
}
