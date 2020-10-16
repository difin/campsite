package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.BookingReference;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Mono<BookingReference> reserve(ReservationPayload payload);
    Reservation reserveInExistingTx(ReservationPayload payload, Optional<String> bookingRef);
    List<Reservation> getReservationsBlocking(Optional<RequestDates> requestDates);
    Flux<ReservationModel> getReservations(Optional<RequestDates> requestDates);
}
