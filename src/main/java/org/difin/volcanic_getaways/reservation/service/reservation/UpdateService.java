package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.internal.UpdateStatus;
import org.difin.volcanic_getaways.reservation.model.response.ActionResult;
import reactor.core.publisher.Mono;

public interface UpdateService {

    Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
    UpdateStatus updateReservationInExistingTx(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
}
