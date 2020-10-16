package campsite.reservation.service;

import campsite.reservation.data.entity.Reservation;
import campsite.reservation.model.in.BookingReferencePayload;
import campsite.reservation.model.in.RequestDates;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.ActionResult;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.model.out.ReservationModel;
import campsite.reservation.service.reservation.CancellationService;
import campsite.reservation.service.reservation.ReservationService;
import campsite.reservation.service.reservation.UpdateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationFacadeImpl implements ReservationFacade {

    private ReservationService reservationService;
    private CancellationService cancellationService;
    private UpdateServiceImpl updateService;

    @Autowired
    public ReservationFacadeImpl(ReservationService reservationService,
                                 CancellationService cancellationService,
                                 UpdateServiceImpl updateService){
        this.reservationService = reservationService;
        this.cancellationService = cancellationService;
        this.updateService = updateService;
    }

    public Mono<BookingReference> reserve(ReservationPayload payload) {
        return reservationService.reserve(payload);
    }

    public Mono<ActionResult> cancelReservation(BookingReferencePayload bookingReferencePayload) {
        return cancellationService.cancelReservation(bookingReferencePayload);
    }

    public Mono<ActionResult> updateReservation(BookingReferencePayload bookingReferencePayload, ReservationPayload payload) {
        return updateService.updateReservation(bookingReferencePayload, payload);
    }

    public Flux<ReservationModel> getReservations(Optional<RequestDates> requestDates){
        return reservationService.getReservations(requestDates);
    }

    public List<Reservation> getReservationsBlocking(Optional<RequestDates> requestDates) {
        return reservationService.getReservationsBlocking(requestDates);
    }

}
