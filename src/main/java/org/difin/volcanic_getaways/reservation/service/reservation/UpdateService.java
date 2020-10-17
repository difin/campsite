package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import reactor.core.publisher.Mono;

public interface UpdateService {

    Mono<Void> updateReservationReactive(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
    boolean updateReservationBlocking(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
}
