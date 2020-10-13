package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.in.RequestDates;
import campsite.reservation.service.availability.AvailableDatesVerifierService;
import campsite.reservation.service.availability.ManagedDatesCreationBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class AvailabilityFacadeImpl implements AvailabilityFacade {

    private AvailableDatesVerifierService availableDatesVerifierService;
    private ManagedDatesCreationBatchService managedDatesCreationBatchService;

    @Autowired
    public AvailabilityFacadeImpl(AvailableDatesVerifierService availableDatesVerifierService,
                                  ManagedDatesCreationBatchService managedDatesCreationBatchService) {

        this.availableDatesVerifierService = availableDatesVerifierService;
        this.managedDatesCreationBatchService = managedDatesCreationBatchService;
    }

   public Flux<AvailableDateModel> getAvailableDates(){
        return availableDatesVerifierService.getAvailableDates();
    }

    public Flux<AvailableDateModel> getAvailableDates(RequestDates requestDates) {
        return availableDatesVerifierService.getAvailableDates(requestDates);
    }

    public List<ManagedDate> getAvailableDatesEagerLocking(RequestDates requestDates) {
        return availableDatesVerifierService.getAvailableDatesEagerLocking(requestDates);
    }

    public List<ManagedDate> getAvailableDatesEagerNotLocking(RequestDates requestDates) {
        return availableDatesVerifierService.getAvailableDatesEagerNotLocking(requestDates);
    }

    public void generateManagedDates() {
        managedDatesCreationBatchService.generateManagedDates();
    }
}
