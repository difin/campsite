package campsite.reservation.service.reservation;

import campsite.reservation.model.ModelConverter;
import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.internal.CancellationStatus;
import campsite.reservation.model.internal.UpdateStatus;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.service.common.ReactiveExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class UpdateServiceImpl implements UpdateService {

    private ReactiveExecutionService reactiveExecutionService;
    private ModelConverter modelConverter;
    private CancellationService cancellationService;
    private ReservationService reservationService;

    @Autowired
    public UpdateServiceImpl(ReactiveExecutionService reactiveExecutionService,
                             ModelConverter modelConverter,
                             CancellationService cancellationService,
                             ReservationService reservationService){
        this.modelConverter = modelConverter;
        this.reactiveExecutionService = reactiveExecutionService;
        this.cancellationService = cancellationService;
        this.reservationService = reservationService;
    }

    public Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload) {
        return reactiveExecutionService.execTransaction(() ->
                updateReservationInPresentTransaction(bookingReferencePayload, payload))
                .map(modelConverter::updateStatusToDTO);
    }

    public UpdateStatus updateReservationInPresentTransaction(BookingReferencePayload bookingReferencePayload,
                                                              ReservationPayload payload) {

        CancellationStatus cancellationStatus = cancellationService.cancelInPresentTransaction(bookingReferencePayload);
        UpdateStatus updateStatus = null;

        switch (cancellationStatus) {
            case NOT_FOUND:
                updateStatus = UpdateStatus.NOT_FOUND;
                break;
            case SUCCESS:
                reservationService.reserveInPresentTransaction(payload, Optional.of(bookingReferencePayload.getBookingReference()));
                updateStatus = UpdateStatus.SUCCESS;
                break;
        }

        return updateStatus;
    }
}
