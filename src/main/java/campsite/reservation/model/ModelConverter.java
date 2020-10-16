package campsite.reservation.model;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.entity.Reservation;
import campsite.reservation.model.internal.CancellationStatus;
import campsite.reservation.model.internal.UpdateStatus;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.model.out.ReservationModel;
import campsite.reservation.service.common.DateConversionService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ModelConverter {

    public AvailableDateModel managedDateEntityToDTO(ManagedDate managedDate) {
        return new AvailableDateModel(managedDate.getDate());
    }

    public BookingReference reservationEntityToBookingReferenceDTO(Reservation reservation) {
        return new BookingReference(reservation.getBookingRef());
    }

    public ActionResult cancellationStatusToDTO(CancellationStatus cancellationStatus) {
        return new ActionResult(cancellationStatus.label);
    }

    public ActionResult updateStatusToDTO(UpdateStatus updateStatus) {
        return new ActionResult(updateStatus.label);
    }

    public ReservationModel reservationEntityToReservationDTO(Reservation reservation) {

        return ReservationModel.builder()
                .id(reservation.getId())
                .name(reservation.getName())
                .email(reservation.getEmail())
                .bookingRef(reservation.getBookingRef())
                .reservedDates(reservation.getReservedDates()
                        .stream()
                        .map(t -> t.getManagedDate().getDate())
                        .sorted()
                        .map(DateConversionService::dateToString)
                        .collect(Collectors.toList()))
                .build();
    }
}
