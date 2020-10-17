package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.data.repository.ReservationRepository;
import org.difin.volcanic_getaways.reservation.data.repository.ReservedDateRepository;
import org.difin.volcanic_getaways.reservation.exception.ReservationNotFoundException;
import org.difin.volcanic_getaways.reservation.model.request.BookingReferencePayload;
import org.difin.volcanic_getaways.reservation.service.common.ReactiveExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CancellationServiceImpl implements CancellationService {

    private ReservationRepository reservationRepository;
    private ReservedDateRepository reservedDateRepository;
    private ReactiveExecutionService reactiveExecutionService;
    MessageSource messageSource;

    @Autowired
    public CancellationServiceImpl(ReservationRepository reservationRepository,
                                  ReservedDateRepository reservedDateRepository,
                                  ReactiveExecutionService reactiveExecutionService,
                                  MessageSource messageSource){
        this.reservationRepository = reservationRepository;
        this.reservedDateRepository = reservedDateRepository;
        this.reactiveExecutionService = reactiveExecutionService;
        this.messageSource = messageSource;
    }

    public Mono<Void> cancelReservationReactive(BookingReferencePayload bookingReferencePayload) {
        return reactiveExecutionService.execTransaction(() ->
                cancelReservationBlocking(bookingReferencePayload))
                .then();
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public boolean cancelReservationBlocking(BookingReferencePayload bookingReferencePayload) {

        Optional<Reservation> reservation =
                Optional.ofNullable(reservationRepository
                        .findByBookingRef(bookingReferencePayload.getBookingReference()));

        reservation
                .ifPresentOrElse(
                        (r) -> {
                            r.getReservedDates()
                                    .forEach(t -> reservedDateRepository.deleteById(t.getId()));

                            reservationRepository.deleteById(r.getId());
                        },
                        () -> {
                            throw new ReservationNotFoundException(
                                    messageSource.getMessage("volcanic_getaways.exception.reservation.not.found",
                                            null, null, Locale.getDefault()));
                        }
                );

        return true;
    }

    @Transactional
    public void deleteAllReservations() {

        List<Reservation> reservations = reservationRepository.findAll();
        reservations.forEach(t -> cancelReservationBlocking(new BookingReferencePayload(t.getBookingRef())));
    }

}
