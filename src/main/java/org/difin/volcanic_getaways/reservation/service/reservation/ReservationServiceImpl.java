package org.difin.volcanic_getaways.reservation.service.reservation;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.data.entity.Reservation;
import org.difin.volcanic_getaways.reservation.data.entity.ReservedDate;
import org.difin.volcanic_getaways.reservation.data.repository.ReservationRepository;
import org.difin.volcanic_getaways.reservation.data.repository.ReservedDateRepository;
import org.difin.volcanic_getaways.reservation.model.ModelConverter;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.request.ReservationPayload;
import org.difin.volcanic_getaways.reservation.model.response.BookingReference;
import org.difin.volcanic_getaways.reservation.model.response.ReservationModel;
import org.difin.volcanic_getaways.reservation.service.ManagedDatesFacade;
import org.difin.volcanic_getaways.reservation.service.common.ReactiveExecutionService;
import org.difin.volcanic_getaways.reservation.validation.MethodParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private ReservedDateRepository reservedDateRepository;
    private ManagedDatesFacade managedDatesFacade;
    private ReactiveExecutionService reactiveExecutionService;
    private ModelConverter modelConverter;
    private MethodParamValidator methodParamValidator;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ReservedDateRepository reservedDateRepository,
                                  ManagedDatesFacade managedDatesFacade,
                                  ReactiveExecutionService reactiveExecutionService,
                                  ModelConverter modelConverter,
                                  MethodParamValidator methodParamValidator){
        this.reservationRepository = reservationRepository;
        this.modelConverter = modelConverter;
        this.managedDatesFacade = managedDatesFacade;
        this.reservedDateRepository = reservedDateRepository;
        this.reactiveExecutionService = reactiveExecutionService;
        this.methodParamValidator = methodParamValidator;
    }

    public Mono<BookingReference> makeReservationReactive(ReservationPayload payload) {

        return reactiveExecutionService.execTransaction(() ->
                makeReservationBlocking(payload, Optional.empty()))
                .map(modelConverter::reservationEntityToBookingReferenceDTO);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Reservation makeReservationBlocking(ReservationPayload payload, Optional<String> bookingRef){

        managedDatesFacade.lockDates(payload.getBookingDates());

        List<ManagedDate> availableDates = managedDatesFacade.getAvailableDatesBlocking(Optional.of(payload.getBookingDates()));

        methodParamValidator.validateSiteAvailability(payload.getBookingDates(), availableDates.size());

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

    public Flux<ReservationModel> getReservationsReactive(Optional<RequestDates> requestDates) {

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

        return reservationRepository
                .findReservationsForDates(requestDates.getArrivalAsDate(), requestDates.getDepartureAsDate());
    }
}
