package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.entity.Reservation;
import campsite.reservation.data.entity.ReservedDate;
import campsite.reservation.data.repository.ReservationRepository;
import campsite.reservation.data.repository.ReservedDateRepository;
import campsite.reservation.model.in.ReservationPayload;
import campsite.reservation.model.out.BookingReference;
import campsite.reservation.model.out.ModelConverter;
import campsite.reservation.validation.BookingDatesValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservedDateRepository reservedDateRepository;
    private final ModelConverter modelConverter;
    private final AvailabilityService availabilityService;
    private final BookingDatesValidator bookingDatesValidator;
    private final TransactionService transactionService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ReservedDateRepository reservedDateRepository,
                                  AvailabilityService availabilityService,
                                  TransactionService transactionService,
                                  ModelConverter modelConverter,
                                  BookingDatesValidator bookingDatesValidator){
        this.reservationRepository = reservationRepository;
        this.modelConverter = modelConverter;
        this.availabilityService = availabilityService;
        this.reservedDateRepository = reservedDateRepository;
        this.bookingDatesValidator = bookingDatesValidator;
        this.transactionService = transactionService;
    }

    public Mono<BookingReference> reserve(ReservationPayload payload) {

        return transactionService.execTransaction(() ->
            Mono
            .fromFuture(
                CompletableFuture.supplyAsync(() ->
                {
                    List<ManagedDate> availableDates = availabilityService.getAvailableDatesBlocking(payload.getBookingDates());

                    bookingDatesValidator.validateCampsiteAvailability(payload.getBookingDates(), availableDates.size());

                    Reservation reservation = new Reservation();

                    reservation.setName(payload.getName());
                    reservation.setEmail(payload.getEmail());
                    reservation.setBookingRef(UUID.randomUUID());

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
                })
            )
            .map(modelConverter::reservationEntityToDTO)
        );
    }
}
