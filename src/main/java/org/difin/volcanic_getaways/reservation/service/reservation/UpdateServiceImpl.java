package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.model.ModelConverter;
import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
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

    public Mono<Void> updateReservationReactive(BookingReferencePayload bookingReferencePayload, ReservationPayload payload) {
        return reactiveExecutionService.execTransaction(() ->
                updateReservationBlocking(bookingReferencePayload, payload))
                .then();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public boolean updateReservationBlocking(BookingReferencePayload bookingReferencePayload,
                                             ReservationPayload payload) {

        if (cancellationService.cancelReservationBlocking(bookingReferencePayload)){

            reservationService.makeReservationBlocking(payload, Optional.of(bookingReferencePayload.getBookingReference()));

            return true;
        }
        else
            return false;
    }
}
