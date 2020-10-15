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

    public List<ManagedDate> lockDates(RequestDates requestDates) {
        return availableDatesVerifierService.lockDates(requestDates);
    }

    public List<ManagedDate> getAvailableDatesBlocking(RequestDates requestDates) {
        return availableDatesVerifierService.getAvailableDatesBlocking(requestDates);
    }

    public void generateManagedDates() {
        managedDatesCreationBatchService.generateManagedDates();
    }
}
