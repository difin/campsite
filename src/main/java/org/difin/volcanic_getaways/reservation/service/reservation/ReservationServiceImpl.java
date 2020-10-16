package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.data.entity.ReservedDate;
import org.difin.volcanic_getaways.reservation.data.repository.ReservationRepository;
import org.difin.volcanic_getaways.reservation.data.repository.ReservedDateRepository;
import org.difin.volcanic_getaways.reservation.exception.VolcanicGetawaysException;
import org.difin.volcanic_getaways.reservation.model.ModelConverter;
import org.difin.volcanic_getaways.reservation.model.in.RequestDates;
import org.difin.volcanic_getaways.reservation.model.in.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.out.BookingReference;
import org.difin.volcanic_getaways.reservation.model.out.ReservationModel;
import org.difin.volcanic_getaways.reservation.service.ManagedDatesFacade;
import org.difin.volcanic_getaways.reservation.service.common.ReactiveExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private ReservedDateRepository reservedDateRepository;
    private ManagedDatesFacade managedDatesFacade;
    private ReactiveExecutionService reactiveExecutionService;
    private ModelConverter modelConverter;
    private MessageSource messageSource;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ReservedDateRepository reservedDateRepository,
                                  ManagedDatesFacade managedDatesFacade,
                                  ReactiveExecutionService reactiveExecutionService,
                                  ModelConverter modelConverter,
                                  MessageSource messageSource){
        this.reservationRepository = reservationRepository;
        this.modelConverter = modelConverter;
        this.managedDatesFacade = managedDatesFacade;
        this.reservedDateRepository = reservedDateRepository;
        this.reactiveExecutionService = reactiveExecutionService;
        this.messageSource = messageSource;
    }

    public Mono<BookingReference> reserve(ReservationPayload payload) {

        return reactiveExecutionService.execTransaction(() ->
                reserveInPresentTransaction(payload, Optional.empty()))
                .map(modelConverter::reservationEntityToBookingReferenceDTO);
    }

    public Reservation reserveInPresentTransaction(ReservationPayload payload, Optional<String> bookingRef){

        managedDatesFacade.lockDates(payload.getBookingDates());

        List<ManagedDate> availableDates = managedDatesFacade.getAvailableDatesBlocking(Optional.of(payload.getBookingDates()));

        validateSiteAvailability(payload.getBookingDates(), availableDates.size());

        Reservation reservation = new Reservation();

        reservation.setName(payload.getName());
        reservation.setEmail(payload.getEmail());

        bookingRef.ifPresentOrElse(
                reservation::setBookingRef,
                () -> reservation.setBookingRef(UUID.randomUUID().toString())
        );

        reservationRepository.save(reservation);

        availableDates
                .forEach(managedDate -> {
                            ReservedDate reservedDate = new ReservedDate();
                            reservedDate.setManagedDate(managedDate);
                            reservedDate.setReservation(reservation);

                            reservedDateRepository.save(reservedDate);
                        }
                );

        return reservation;
    }

    private void validateSiteAvailability(RequestDates requestDates, int availableDaysCount){

        if (DAYS.between(requestDates.getArrivalAsDate(), requestDates.getDepartureAsDate()) > availableDaysCount){

            throw new VolcanicGetawaysException(
                    messageSource.getMessage("volcanic_getaways.exception.reservation.at.full.capacity", null, null, Locale.getDefault())
            );
        }
    }

    public Flux<ReservationModel> getReservations(Optional<RequestDates> requestDates) {

        return reactiveExecutionService.exec(() ->
                getReservationsBlocking(requestDates))
                .flatMapIterable(t -> t)
                .map(modelConverter::reservationEntityToReservationDTO);
    }

    public List<Reservation> getReservationsBlocking(Optional<RequestDates> requestDatesOptional) {

        RequestDates requestDates = requestDatesOptional.orElse(
                new RequestDates(
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusMonths(1)));

        List<Reservation> reservations = reservationRepository
                .findReservationsForDates(requestDates.getArrivalAsDate(), requestDates.getDepartureAsDate());

        return reservations;
    }
}
