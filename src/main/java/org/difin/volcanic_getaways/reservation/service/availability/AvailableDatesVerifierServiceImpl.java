package org.difin.volcanic_getaways.reservation.service.availability;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.data.repository.CustomManagedDateRepository;
import org.difin.volcanic_getaways.reservation.model.ModelConverter;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.response.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.service.common.ReactiveExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class AvailableDatesVerifierServiceImpl implements AvailableDatesVerifierService {

    private CustomManagedDateRepository customManagedDateRepository;
    private ReactiveExecutionService reactiveExecutionService;
    private ModelConverter modelConverter;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${app.spots-num}")
    private int spotsNum;

    @Autowired
    public AvailableDatesVerifierServiceImpl(CustomManagedDateRepository customManagedDateRepository,
                                             ReactiveExecutionService reactiveExecutionService,
                                             ModelConverter modelConverter) {

        this.customManagedDateRepository = customManagedDateRepository;
        this.reactiveExecutionService = reactiveExecutionService;
        this.modelConverter = modelConverter;
    }

    public Flux<AvailableDateModel> getAvailableDatesReactive(RequestDates requestDatesOptional, boolean lock) {

        return reactiveExecutionService.exec(() -> getAvailableDatesBlocking(requestDatesOptional, lock))
                .flatMapIterable(t -> t)
                .map(modelConverter::managedDateEntityToDTO);
    }

    public List<ManagedDate> getAvailableDatesBlocking(RequestDates requestDates, boolean lock) {

        LOGGER.debug("getAvailableDatesBlocking - enter; for range=" + requestDates.getArrival() + "," + requestDates.getDeparture());

        List<ManagedDate> result =
            customManagedDateRepository.getAvailableDates(spotsNum,
                requestDates.getArrival(),
                requestDates.getDeparture().minusDays(1), lock);

        LOGGER.debug("getAvailableDatesBlocking - exit; found " + result.size() + " available dates");

        return result;
    }
}
