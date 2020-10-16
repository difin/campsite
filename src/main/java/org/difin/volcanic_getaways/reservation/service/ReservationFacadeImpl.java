package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.ActionResult;
import org.difin.volcanic_getaways.reservation.model.response.BookingReference;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import org.difin.volcanic_getaways.reservation.service.reservation.CancellationService;
import org.difin.volcanic_getaways.reservation.service.reservation.ReservationService;
import org.difin.volcanic_getaways.reservation.service.reservation.UpdateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ReservationFacadeImpl implements ReservationFacade {

    private ReservationService reservationService;
    private CancellationService cancellationService;
    private UpdateServiceImpl updateService;

    @Autowired
    public ReservationFacadeImpl(ReservationService reservationService,
                                 CancellationService cancellationService,
                                 UpdateServiceImpl updateService){
        this.reservationService = reservationService;
        this.cancellationService = cancellationService;
        this.updateService = updateService;
    }

    public Mono<BookingReference> makeReservation(ReservationPayload payload) {
        return reservationService.makeReservationReactive(payload);
    }

    public Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload) {
        return cancellationService.cancelReservationReactive(bookingReferencePayload);
    }

    public Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload) {
        return updateService.updateReservationReactive(bookingReferencePayload, payload);
    }

    public Flux<ReservationModel> getReservations(Optional<RequestDates> requestDates){
        return reservationService.getReservationsReactive(requestDates);
    }
}
