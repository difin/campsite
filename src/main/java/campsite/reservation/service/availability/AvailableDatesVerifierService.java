package campsite.reservation.service.availability;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.model.in.RequestDates;
import campsite.reservation.model.out.AvailableDateModel;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface AvailableDatesVerifierService {

    Flux<AvailableDateModel> getAvailableDates(Optional<RequestDates> requestDates);
    List<ManagedDate> getAvailableDatesBlocking(Optional<RequestDates> requestDates);
    List<ManagedDate> lockDates(RequestDates requestDates);
}
