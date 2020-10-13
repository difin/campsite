package campsite.reservation.service.availability;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.model.in.RequestDates;
import campsite.reservation.model.out.AvailableDateModel;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AvailableDatesVerifierService {

    Flux<AvailableDateModel> getAvailableDates();
    Flux<AvailableDateModel> getAvailableDates(RequestDates requestDates);
    List<ManagedDate> getAvailableDatesEagerLocking(RequestDates requestDates);
    List<ManagedDate> getAvailableDatesEagerNotLocking(RequestDates requestDates);
}
