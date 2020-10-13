package campsite.reservation.service.reservation;

import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.internal.CancellationStatus;
import campsite.reservation.model.out.ActionResult;
import reactor.core.publisher.Mono;

public interface CancellationService {

    Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload);
    CancellationStatus cancelInPresentTransaction(BookingReferencePayload bookingReferencePayload);
}
