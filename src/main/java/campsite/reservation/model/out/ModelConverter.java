package campsite.reservation.model.out;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ModelConverter {

    public AvailableDateModel managedDateEntityToDTO(ManagedDate managedDate) {
        return new AvailableDateModel(managedDate.getDate());
    }

    public BookingReference reservationEntityToDTO(Reservation reservation) {
        return new BookingReference(reservation.getBookingRef());
    }
}
