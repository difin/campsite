package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.model.response.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.service.availability.AvailableDatesVerifierService;
import org.difin.volcanic_getaways.reservation.service.availability.ManagedDatesCreationBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

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

    public Flux<AvailableDateModel> getAvailableDatesReactive(RequestDates requestDates, boolean lock) {
        return availableDatesVerifierService.getAvailableDatesReactive(requestDates, lock);
    }

    public List<ManagedDate> getAvailableDatesBlocking(RequestDates requestDates, boolean lock) {
        return availableDatesVerifierService.getAvailableDatesBlocking(requestDates, lock);
    }

    public void generateManagedDatesBlocking() {
        managedDatesCreationBatchService.generateManagedDates();
    }
}
