package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.BookingReferenceModel;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ReservationFacade {

   Mono<BookingReferenceModel> makeReservationReactive(ReservationPayload payload);
   Mono<Void> cancelReservationReactive(BookingReferencePayload bookingReferencePayload);
   Mono<Void> updateReservationReactive(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
   Flux<ReservationModel> getReservationsReactive(RequestDates requestDates);

   Reservation makeReservationBlocking(ReservationPayload payload, Optional<String> bookingRef);
   boolean cancelReservationBlocking(BookingReferencePayload bookingReferencePayload);
   void cancelAllReservationsBlocking();
   boolean updateReservationBlocking(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
   List<Reservation> getReservationsBlocking(RequestDates requestDates);
   List<Reservation> getReservationsBlocking();
}
