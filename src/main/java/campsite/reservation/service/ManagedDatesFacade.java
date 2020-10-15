package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.in.RequestDates;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface ManagedDatesFacade {

   Flux<AvailableDateModel> getAvailableDates(Optional<RequestDates> requestDates);
   List<ManagedDate> getAvailableDatesBlocking(RequestDates requestDates);
   List<ManagedDate> lockDates(RequestDates requestDates);
   void generateManagedDates();
}
