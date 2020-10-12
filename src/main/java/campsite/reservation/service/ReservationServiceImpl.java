package campsite.reservation.service;

import campsite.reservation.data.entity.Reservation;
import campsite.reservation.data.repository.ReservationRepository;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.model.out.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ModelConverter modelConverter;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ModelConverter modelConverter){
        this.reservationRepository = reservationRepository;
        this.modelConverter = modelConverter;
    }

    public Mono<BookingReference> reserve(ReservationPayload payload) {

        return Mono
                .fromFuture(
                        CompletableFuture.supplyAsync(() ->
                        {
                            Reservation reservation = new Reservation();

                            reservation.setName(payload.getName());
                            reservation.setEmail(payload.getEmail());
                            reservation.setBookingRef(UUID.randomUUID());

                            reservationRepository.save(reservation);
                            return reservation;
                        })
                )
//                .flatMapIterable(t -> t)
                .map(modelConverter::reservationEntityToDTO);
    }
}
