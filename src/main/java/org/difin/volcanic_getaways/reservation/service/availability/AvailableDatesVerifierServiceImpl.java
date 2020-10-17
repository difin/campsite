package org.difin.volcanic_getaways.reservation.service.availability;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.data.repository.ManagedDateRepository;
import org.difin.volcanic_getaways.reservation.model.ModelConverter;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.response.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.service.common.ReactiveExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AvailableDatesVerifierServiceImpl implements AvailableDatesVerifierService {

    private ManagedDateRepository managedDateRepository;
    private ReactiveExecutionService reactiveExecutionService;
    private ModelConverter modelConverter;

    private int spotsNum;

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

    public Flux<AvailableDateModel> getAvailableDates(Optional<RequestDates> requestDatesOptional) {

        return reactiveExecutionService.exec(() -> getAvailableDatesBlocking(requestDatesOptional))
                .flatMapIterable(t -> t)
                .map(modelConverter::managedDateEntityToDTO);
    }

    public List<ManagedDate> lockDates(RequestDates requestDates) {

        return managedDateRepository.lockDates(
                requestDates.getArrival(),
                requestDates.getDeparture().minusDays(1));
    }

    public List<ManagedDate> getAvailableDatesBlocking(Optional<RequestDates> requestDatesOptional) {

        RequestDates requestDates = requestDatesOptional.orElse(
                new RequestDates(
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusMonths(1))
        );

        return managedDateRepository.getAvailableDates(spotsNum,
                requestDates.getArrival(),
                requestDates.getDeparture().minusDays(1));
    }
}
