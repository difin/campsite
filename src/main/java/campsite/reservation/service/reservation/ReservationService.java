package campsite.reservation.service.reservation;

import campsite.reservation.data.entity.Reservation;
import campsite.reservation.model.in.RequestDates;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.model.out.ReservationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Mono<BookingReference> reserve(ReservationPayload payload);
    Reservation reserveInPresentTransaction(ReservationPayload payload, Optional<String> bookingRef);
    List<Reservation> getReservationsBlocking(Optional<RequestDates> requestDates);
    Flux<ReservationModel> getReservations(Optional<RequestDates> requestDates);
}
