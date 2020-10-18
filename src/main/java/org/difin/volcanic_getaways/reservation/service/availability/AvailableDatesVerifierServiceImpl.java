package org.difin.volcanic_getaways.reservation.service.availability;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.data.repository.ManagedDateRepository;
import org.difin.volcanic_getaways.reservation.model.ModelConverter;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.response.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.service.common.ReactiveExecutionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
public class AvailableDatesVerifierServiceImpl implements AvailableDatesVerifierService {

    private ManagedDateRepository managedDateRepository;
    private ReactiveExecutionService reactiveExecutionService;
    private ModelConverter modelConverter;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${app.spots-num}")
    private int spotsNum;

    @Autowired
    public AvailableDatesVerifierServiceImpl(ManagedDateRepository managedDateRepository,
                                  ReactiveExecutionService reactiveExecutionService,
                                  ModelConverter modelConverter) {

        this.managedDateRepository = managedDateRepository;
        this.reactiveExecutionService = reactiveExecutionService;
        this.modelConverter = modelConverter;
    }

    public Flux<AvailableDateModel> getAvailableDatesReactive(Optional<RequestDates> requestDatesOptional) {

        return reactiveExecutionService.exec(() -> getAvailableDatesBlocking(requestDatesOptional))
                .flatMapIterable(t -> t)
                .map(modelConverter::managedDateEntityToDTO);
    }

    @Transactional(propagation=MANDATORY, timeout=2)
    public List<ManagedDate> lockDates(RequestDates requestDates) {

        LOGGER.trace("lockDates - enter");

        List<ManagedDate> dates =
            managedDateRepository.lockDates(
                requestDates.getArrival(),
                requestDates.getDeparture().minusDays(1));

        LOGGER.trace("lockDates - exit");

        return dates;
    }

    public List<ManagedDate> getAvailableDatesBlocking(Optional<RequestDates> requestDatesOptional) {

        LOGGER.debug("getAvailableDatesBlocking - enter; for range=" + requestDatesOptional.map(
                t -> { return t.getArrival() + "," + t.getDeparture(); }));

        RequestDates requestDates = requestDatesOptional.orElse(
                new RequestDates(
                        LocalDate.now().plusDays(1),
                        LocalDate.now().plusMonths(1))
        );

        List<ManagedDate> result =
            managedDateRepository.getAvailableDates(spotsNum,
                requestDates.getArrival(),
                requestDates.getDeparture().minusDays(1));

        LOGGER.debug("getAvailableDatesBlocking - exit; found " + result.size() + " available dates");

        return result;
    }
}
