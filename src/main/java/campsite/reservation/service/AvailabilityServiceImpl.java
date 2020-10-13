package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.repository.ManagedDateRepository;
import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.ModelConverter;
import campsite.reservation.model.in.RequestDates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {

    private final ManagedDateRepository managedDateRepository;
    private final ReactiveExecutionService reactiveExecutionService;
    private final int spotsNum;
    private final ModelConverter modelConverter;

    @Autowired
    public AvailabilityServiceImpl(ManagedDateRepository managedDateRepository,
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

    @Scheduled(cron = "0 0 0 * * *")
    public void generateManagedDates() {

        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusMonths(1);

        List<LocalDate> existingDates =
                managedDateRepository
                        .getManagedDates(start, end)
                        .stream()
                        .map(ManagedDate::getDate)
                        .collect(Collectors.toList());

        List<LocalDate> allDates =
                Stream.iterate(start, date -> date.plusDays(1))
                    .limit(DAYS.between(start, end))
                    .collect(Collectors.toList());

        allDates.removeAll(existingDates);

        allDates
                .forEach(t -> {
                    ManagedDate managedDate = new ManagedDate();
                    managedDate.setDate(t);
                    managedDateRepository.save(managedDate);
                });
    }
}
