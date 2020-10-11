package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.repository.ManagedDateRepository;
import campsite.reservation.model.AvailableDateModel;
import campsite.reservation.model.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Service
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
                LocalDate.now().plusDays(1),
                LocalDate.now().plusMonths(1)
            );
    }

    public Flux<AvailableDateModel> getAvailableDates(LocalDate startDate, LocalDate endDate){

        return Mono
                .fromFuture(
                        CompletableFuture.supplyAsync(() -> managedDateRepository.getAvailableDates(spotsNum, startDate, endDate))
                )
                .flatMapIterable(t -> t)
                .map(t -> modelConverter.managedDateEntityToDTO(t));
    }
}
