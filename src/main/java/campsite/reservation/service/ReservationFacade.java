package campsite.reservation.service;

import campsite.reservation.data.entity.Reservation;
import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.RequestDates;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.model.out.ReservationModel;
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
