package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import reactor.core.publisher.Mono;

public interface CancellationService {

    Mono<Void> cancelReservationReactive(BookingReferencePayload bookingReferencePayload);
    boolean cancelReservationBlocking(BookingReferencePayload bookingReferencePayload);
    void cancelAllReservationsBlocking();
}
