package campsite.reservation.service.reservation;

import campsite.reservation.data.entity.Reservation;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.BookingReference;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ReservationService {

    Mono<BookingReference> reserve(ReservationPayload payload);
    Reservation reserveInPresentTransaction(ReservationPayload payload, Optional<String> bookingRef);
}
