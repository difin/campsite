package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.model.ModelConverter;
import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.internal.CancellationStatus;
import org.difin.volcanic_getaways.reservation.model.internal.UpdateStatus;
import org.difin.volcanic_getaways.reservation.model.response.ActionResult;
import org.difin.volcanic_getaways.reservation.service.common.ReactiveExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
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

    public Mono<ActionResult> updateReservationReactive(BookingReferencePayload bookingReferencePayload, ReservationPayload payload) {
        return reactiveExecutionService.execTransaction(() ->
                updateReservationBlocking(bookingReferencePayload, payload))
                .map(modelConverter::updateStatusToDTO);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public UpdateStatus updateReservationBlocking(BookingReferencePayload bookingReferencePayload,
                                                  ReservationPayload payload) {

        CancellationStatus cancellationStatus = cancellationService.cancelReservationBlocking(bookingReferencePayload);
        UpdateStatus updateStatus = null;

        switch (cancellationStatus) {
            case NOT_FOUND:
                updateStatus = UpdateStatus.NOT_FOUND;
                break;
            case SUCCESS:
                reservationService.makeReservationBlocking(payload, Optional.of(bookingReferencePayload.getBookingReference()));
                updateStatus = UpdateStatus.SUCCESS;
                break;
        }

        return updateStatus;
    }
}
