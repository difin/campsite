package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.ActionResult;
import org.difin.volcanic_getaways.reservation.model.response.BookingReference;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ReservationFacade {

   Mono<BookingReference> makeReservation(ReservationPayload payload);
   Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload);
   Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
   Flux<ReservationModel> getReservations(Optional<RequestDates> requestDates);
}
