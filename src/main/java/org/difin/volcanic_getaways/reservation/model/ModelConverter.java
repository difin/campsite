package org.difin.volcanic_getaways.reservation.model;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.model.response.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.model.response.BookingReferenceModel;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ModelConverter {

    public AvailableDateModel managedDateEntityToDTO(ManagedDate managedDate) {
        return new AvailableDateModel(managedDate.getDate());
    }

    public BookingReferenceModel reservationEntityToBookingReferenceDTO(Reservation reservation) {
        return new BookingReferenceModel(reservation.getBookingRef());
    }

    public ReservationModel reservationEntityToReservationDTO(Reservation reservation) {

        return ReservationModel.builder()
                .name(reservation.getName())
                .email(reservation.getEmail())
                .bookingRef(reservation.getBookingRef())
                .reservedDates(reservation.getReservedDates()
                        .stream()
                        .map(t -> t.getManagedDate().getDate())
                        .sorted()
                        .collect(Collectors.toList()))
                .build();
    }
}
