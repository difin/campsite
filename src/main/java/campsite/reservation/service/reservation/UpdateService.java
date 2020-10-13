package campsite.reservation.service.reservation;

import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.internal.UpdateStatus;
import campsite.reservation.model.out.ActionResult;
import reactor.core.publisher.Mono;

public interface UpdateService {

    Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
    UpdateStatus updateReservationInPresentTransaction(BookingReferencePayload bookingReferencePayload, ReservationPayload payload);
}
