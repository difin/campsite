package campsite.reservation.service.availability;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.repository.ManagedDateRepository;
import campsite.reservation.model.ModelConverter;
import campsite.reservation.model.in.RequestDates;
import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.service.common.ReactiveExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;

@Service
public class AvailableDatesVerifierServiceImpl implements AvailableDatesVerifierService {

    private final ManagedDateRepository managedDateRepository;
    private final ReactiveExecutionService reactiveExecutionService;
    private final int spotsNum;
    private final ModelConverter modelConverter;

    @Autowired
    public AvailableDatesVerifierServiceImpl(ManagedDateRepository managedDateRepository,
                                  ReactiveExecutionService reactiveExecutionService,
                                  ModelConverter modelConverter,
                                  @Value("${app.spots-num}") int spotsNum) {

        this.managedDateRepository = managedDateRepository;
        this.reactiveExecutionService = reactiveExecutionService;
        this.modelConverter = modelConverter;
        this.spotsNum = spotsNum;
    }

    public Flux<AvailableDateModel> getAvailableDates(){

        return getAvailableDates(
                new RequestDates(
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusMonths(1))
        );
    }

    public Flux<AvailableDateModel> getAvailableDates(RequestDates requestDates) {

        return reactiveExecutionService.exec(() -> getAvailableDatesEagerNotLocking(requestDates))
                .flatMapIterable(t -> t)
                .map(modelConverter::managedDateEntityToDTO);
    }

    public List<ManagedDate> getAvailableDatesEagerLocking(RequestDates requestDates) {

        return managedDateRepository.getAvailableDatesLocking(spotsNum,
                requestDates.getArrivalAsDate(),
                requestDates.getDepartureAsDate().minusDays(1));
    }

    public List<ManagedDate> getAvailableDatesEagerNotLocking(RequestDates requestDates) {

        return managedDateRepository.getAvailableDatesNotLocking(spotsNum,
                requestDates.getArrivalAsDate(),
                requestDates.getDepartureAsDate().minusDays(1));
    }
}
