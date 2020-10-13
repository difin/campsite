package campsite.reservation.service.reservation;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.entity.Reservation;
import campsite.reservation.data.entity.ReservedDate;
import campsite.reservation.data.repository.ReservationRepository;
import campsite.reservation.data.repository.ReservedDateRepository;
import campsite.reservation.model.ModelConverter;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.service.AvailabilityFacade;
import campsite.reservation.service.common.ReactiveExecutionService;
import campsite.reservation.validation.MethodParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;
    private ReservedDateRepository reservedDateRepository;
    private AvailabilityFacade availabilityFacade;
    private ReactiveExecutionService reactiveExecutionService;
    private ModelConverter modelConverter;
    private MethodParamValidator methodParamValidator;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ReservedDateRepository reservedDateRepository,
                                  AvailabilityFacade availabilityFacade,
                                  ReactiveExecutionService reactiveExecutionService,
                                  ModelConverter modelConverter,
                                  MethodParamValidator methodParamValidator){
        this.reservationRepository = reservationRepository;
        this.modelConverter = modelConverter;
        this.availabilityFacade = availabilityFacade;
        this.reservedDateRepository = reservedDateRepository;
        this.methodParamValidator = methodParamValidator;
        this.reactiveExecutionService = reactiveExecutionService;
    }

    public Mono<BookingReference> reserve(ReservationPayload payload) {

        return reactiveExecutionService.execTransaction(() ->
                reserveInPresentTransaction(payload, Optional.empty()))
                .map(modelConverter::reservationEntityToDTO);
    }

    public Reservation reserveInPresentTransaction(ReservationPayload payload, Optional<String> bookingRef){

        List<ManagedDate> availableDates = availabilityFacade.getAvailableDatesEagerLocking(payload.getBookingDates());

        methodParamValidator.validateCampsiteAvailability(payload.getBookingDates(), availableDates.size());

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
                            reservedDate.setReservationId(reservation);

                            reservedDateRepository.save(reservedDate);
                        }
                );

        return reservation;
    }
}
