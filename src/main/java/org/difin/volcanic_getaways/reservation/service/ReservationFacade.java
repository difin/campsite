package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.BookingReferenceModel;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReservationFacade {

   Mono<BookingReferenceModel> makeReservation(ReservationPayload payload);
   Mono<Void> cancelReservation(BookingReferencePayload bookingReferencePayload);
   Mono<Void> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
   Flux<ReservationModel> getReservations(RequestDates requestDates);
}
