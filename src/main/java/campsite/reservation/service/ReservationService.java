package campsite.reservation.service;

import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.model.out.BookingReference;
import reactor.core.publisher.Mono;

public interface ReservationService {

   Mono<BookingReference> reserve(ReservationPayload payload);
   Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload);
   Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
}
