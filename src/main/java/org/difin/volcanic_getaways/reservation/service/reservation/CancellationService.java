package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.model.in.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.internal.CancellationStatus;
import org.difin.volcanic_getaways.reservation.model.out.ActionResult;
import reactor.core.publisher.Mono;

public interface CancellationService {

    Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload);
    CancellationStatus cancelInPresentTransaction(BookingReferencePayload bookingReferencePayload);

    // Service for internal use only, not in facade
    void deleteAllReservations();
}
