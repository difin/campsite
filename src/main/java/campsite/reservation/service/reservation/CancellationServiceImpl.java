package campsite.reservation.service.reservation;

import campsite.reservation.data.entity.Reservation;
import campsite.reservation.data.repository.ReservationRepository;
import campsite.reservation.data.repository.ReservedDateRepository;
import campsite.reservation.model.ModelConverter;
import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.internal.CancellationStatus;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.service.ReactiveExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CancellationServiceImpl implements CancellationService {

    private final ReservationRepository reservationRepository;
    private final ReservedDateRepository reservedDateRepository;
    private final ReactiveExecutionService reactiveExecutionService;
    private final ModelConverter modelConverter;

    @Autowired
    public CancellationServiceImpl(ReservationRepository reservationRepository,
                                  ReservedDateRepository reservedDateRepository,
                                  ReactiveExecutionService reactiveExecutionService,
                                  ModelConverter modelConverter){
        this.reservationRepository = reservationRepository;
        this.modelConverter = modelConverter;
        this.reservedDateRepository = reservedDateRepository;
        this.reactiveExecutionService = reactiveExecutionService;
    }

    public Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload) {
        return reactiveExecutionService.execTransaction(() ->
                cancelInPresentTransaction(bookingReferencePayload))
                .map(modelConverter::cancellationStatusToDTO);
    }

    public CancellationStatus cancelInPresentTransaction(BookingReferencePayload bookingReferencePayload) {

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

}
