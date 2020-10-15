package campsite.reservation.service.reservation;

import campsite.reservation.data.entity.Reservation;
import campsite.reservation.data.repository.ReservationRepository;
import campsite.reservation.data.repository.ReservedDateRepository;
import campsite.reservation.exception.CampsiteException;
import campsite.reservation.model.ModelConverter;
import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.internal.CancellationStatus;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.service.common.ReactiveExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CancellationServiceImpl implements CancellationService {

    private ReservationRepository reservationRepository;
    private ReservedDateRepository reservedDateRepository;
    private ReactiveExecutionService reactiveExecutionService;
    private ModelConverter modelConverter;

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

    @Transactional
    public void deleteAllReservations() {

        List<Reservation> reservations = reservationRepository.findAll();
        reservations.forEach(t -> {
            CancellationStatus cancellationStatus =
                    cancelInPresentTransaction(new BookingReferencePayload(t.getBookingRef()));

            if (cancellationStatus != CancellationStatus.SUCCESS){
                throw new CampsiteException("Failed deleting all reservations");
            }
        });
    }

}
