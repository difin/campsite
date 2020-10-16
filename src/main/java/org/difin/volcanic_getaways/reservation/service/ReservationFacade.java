package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.model.in.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.in.RequestDates;
import org.difin.volcanic_getaways.reservation.model.in.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.out.ActionResult;
import org.difin.volcanic_getaways.reservation.model.out.BookingReference;
import org.difin.volcanic_getaways.reservation.model.out.ReservationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ReservationFacade {

   Mono<BookingReference> reserve(ReservationPayload payload);
   Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload);
   Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
   Flux<ReservationModel> getReservations(Optional<RequestDates> requestDates);
   List<Reservation> getReservationsBlocking(Optional<RequestDates> requestDates);
}
