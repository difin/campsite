package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.model.out.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.model.in.RequestDates;
import org.difin.volcanic_getaways.reservation.service.availability.AvailableDatesVerifierService;
import org.difin.volcanic_getaways.reservation.service.availability.ManagedDatesCreationBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@Service
public class ManagedDatesFacadeImpl implements ManagedDatesFacade {

    private AvailableDatesVerifierService availableDatesVerifierService;
    private ManagedDatesCreationBatchService managedDatesCreationBatchService;

    @Autowired
    public ManagedDatesFacadeImpl(AvailableDatesVerifierService availableDatesVerifierService,
                                  ManagedDatesCreationBatchService managedDatesCreationBatchService) {

        this.availableDatesVerifierService = availableDatesVerifierService;
        this.managedDatesCreationBatchService = managedDatesCreationBatchService;
    }

    public Flux<AvailableDateModel> getAvailableDates(Optional<RequestDates> requestDates) {
        return availableDatesVerifierService.getAvailableDates(requestDates);
    }

    public List<ManagedDate> getAvailableDatesBlocking(Optional<RequestDates> requestDates) {
        return availableDatesVerifierService.getAvailableDatesBlocking(requestDates);
    }

    public List<ManagedDate> lockDates(RequestDates requestDates) {
        return availableDatesVerifierService.lockDates(requestDates);
    }

    public void generateManagedDates() {
        managedDatesCreationBatchService.generateManagedDates();
    }
}
