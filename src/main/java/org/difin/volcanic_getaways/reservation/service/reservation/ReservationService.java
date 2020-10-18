package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.BookingReferenceModel;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    Mono<BookingReferenceModel> makeReservationReactive(ReservationPayload payload);
    Reservation makeReservationBlocking(ReservationPayload payload, Optional<String> bookingRef);

    Flux<ReservationModel> getReservationsReactive(RequestDates requestDates);
    List<Reservation> getReservationsBlocking(RequestDates requestDates);
    List<Reservation> getReservationsBlocking();
}
