package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.BookingReferenceModel;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import org.difin.volcanic_getaways.reservation.service.reservation.CancellationService;
import org.difin.volcanic_getaways.reservation.service.reservation.ReservationService;
import org.difin.volcanic_getaways.reservation.service.reservation.UpdateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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

    public Mono<BookingReferenceModel> makeReservationReactive(ReservationPayload payload) {
        return reservationService.makeReservationReactive(payload);
    }

    public Mono<Void> cancelReservationReactive(BookingReferencePayload bookingReferencePayload) {
        return cancellationService.cancelReservationReactive(bookingReferencePayload);
    }

    public Mono<Void> updateReservationReactive(BookingReferencePayload bookingReferencePayload, ReservationPayload payload) {
        return updateService.updateReservationReactive(bookingReferencePayload, payload);
    }

    public Flux<ReservationModel> getReservationsReactive(RequestDates requestDates){
        return reservationService.getReservationsReactive(requestDates);
    }

    public Reservation makeReservationBlocking(ReservationPayload payload, Optional<String> bookingRef){
        return reservationService.makeReservationBlocking(payload, bookingRef);
    }

    public boolean cancelReservationBlocking(BookingReferencePayload bookingReferencePayload){
        return cancellationService.cancelReservationBlocking(bookingReferencePayload);
    }

    public boolean updateReservationBlocking(BookingReferencePayload bookingReferencePayload, ReservationPayload reservationPayload){
        return updateService.updateReservationBlocking(bookingReferencePayload, reservationPayload);
    }

    public List<Reservation> getReservationsBlocking(RequestDates requestDates){
        return reservationService.getReservationsBlocking(requestDates);
    }

    public List<Reservation> getReservationsBlocking(){
        return reservationService.getReservationsBlocking();
    }

    public void cancelAllReservationsBlocking(){
        cancellationService.cancelAllReservationsBlocking();
    }
}
