package campsite.reservation.service;

import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.BookingReference;
import reactor.core.publisher.Mono;

public interface ReservationService {

   Mono<BookingReference> reserve(ReservationPayload payload);
}
