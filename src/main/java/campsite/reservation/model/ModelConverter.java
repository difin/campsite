package campsite.reservation.model;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.entity.Reservation;
import campsite.reservation.model.internal.CancellationStatus;
import campsite.reservation.model.internal.UpdateStatus;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.out.BookingReference;
import org.springframework.stereotype.Component;

@Component
public class ModelConverter {

    public AvailableDateModel managedDateEntityToDTO(ManagedDate managedDate) {
        return new AvailableDateModel(managedDate.getDate());
    }

    public BookingReference reservationEntityToDTO(Reservation reservation) {
        return new BookingReference(reservation.getBookingRef());
    }

    public ActionResult cancellationStatusToDTO(CancellationStatus cancellationStatus) {
        return new ActionResult(cancellationStatus.label);
    }

    public ActionResult updateStatusToDTO(UpdateStatus updateStatus) {
        return new ActionResult(updateStatus.label);
    }
}
