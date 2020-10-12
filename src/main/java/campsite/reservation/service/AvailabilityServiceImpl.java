package campsite.reservation.service;

import campsite.reservation.data.repository.ManagedDateRepository;
import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.out.ModelConverter;
import campsite.reservation.model.in.RequestedDatesRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
@Validated
public class AvailabilityServiceImpl implements AvailabilityService {

    private final ManagedDateRepository managedDateRepository;
    private final int spotsNum;
    private final ModelConverter modelConverter;

    @Autowired
    public AvailabilityServiceImpl(ManagedDateRepository managedDateRepository,
                                   ModelConverter modelConverter,
                                   @Value("${app.spots-num}") int spotsNum) {

        this.managedDateRepository = managedDateRepository;
        this.modelConverter = modelConverter;
        this.spotsNum = spotsNum;
    }

   public Flux<AvailableDateModel> getAvailableDates(){

        return getAvailableDates(
                new RequestedDatesRange(
                    LocalDate.now().plusDays(1),
                    LocalDate.now().plusMonths(1))
            );
    }

    public Flux<AvailableDateModel> getAvailableDates(@Valid RequestedDatesRange availableDatesRange) {

        return Mono
                .fromFuture(
                        CompletableFuture.supplyAsync(() ->
                                managedDateRepository.getAvailableDates(spotsNum,
                                        availableDatesRange.getStartDate(),
                                        availableDatesRange.getEndDate())))
                .flatMapIterable(t -> t)
                .map(modelConverter::managedDateEntityToDTO);
    }
}
