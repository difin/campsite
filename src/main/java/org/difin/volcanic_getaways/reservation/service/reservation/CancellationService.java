package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.internal.CancellationStatus;
import org.difin.volcanic_getaways.reservation.model.response.ActionResult;
import reactor.core.publisher.Mono;

public interface CancellationService {

    Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload);
    CancellationStatus cancelReservationInExistingTx(BookingReferencePayload bookingReferencePayload);

    // Service for internal use only, not in facade
    void deleteAllReservations();
}
