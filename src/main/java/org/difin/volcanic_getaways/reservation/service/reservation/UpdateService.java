package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.model.in.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.model.in.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.internal.UpdateStatus;
import org.difin.volcanic_getaways.reservation.model.out.ActionResult;
import reactor.core.publisher.Mono;

public interface UpdateService {

    Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
    UpdateStatus updateReservationInPresentTransaction(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
}
