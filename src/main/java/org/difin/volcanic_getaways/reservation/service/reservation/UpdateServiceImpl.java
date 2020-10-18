package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.service.common.ReactiveExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Service
public class UpdateServiceImpl implements UpdateService {

    private ReactiveExecutionService reactiveExecutionService;
    private CancellationService cancellationService;
    private ReservationService reservationService;

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UpdateServiceImpl(ReactiveExecutionService reactiveExecutionService,
                             CancellationService cancellationService,
                             ReservationService reservationService){
        this.reactiveExecutionService = reactiveExecutionService;
        this.cancellationService = cancellationService;
        this.reservationService = reservationService;
    }

    public Mono<Void> updateReservationReactive(BookingReferencePayload bookingReferencePayload, ReservationPayload payload) {
        return reactiveExecutionService.execTransaction(() ->
                updateReservationBlocking(bookingReferencePayload, payload))
                .then();
    }

    @Transactional(propagation=REQUIRED, timeout=3)
    public boolean updateReservationBlocking(BookingReferencePayload bookingReferencePayload,
                                             ReservationPayload payload) {

        LOGGER.debug("updateReservationBlocking - enter; payload: [name=" + payload.getName() + ", email=" + payload.getEmail() +
                ", arrival=" + payload.getBookingDates().getArrival() + ", departure=" + payload.getBookingDates().getDeparture() + "]" +
                "; bookingRef=[" + bookingReferencePayload.getBookingReference() + "]");

        boolean result;

        if (cancellationService.cancelReservationBlocking(bookingReferencePayload)){

            reservationService.makeReservationBlocking(payload, Optional.of(bookingReferencePayload.getBookingReference()));

            result = true;
        }
        else
            result = false;

        LOGGER.debug("updateReservationBlocking - exit; sucess=[" + result + "]");

        return result;
    }
}
