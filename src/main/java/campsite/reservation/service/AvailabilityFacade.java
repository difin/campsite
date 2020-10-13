package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.in.RequestDates;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AvailabilityFacade {

   Flux<AvailableDateModel> getAvailableDates();
   Flux<AvailableDateModel> getAvailableDates(RequestDates requestDates);
   List<ManagedDate> getAvailableDatesEagerLocking(RequestDates requestDates);
   List<ManagedDate> getAvailableDatesEagerNotLocking(RequestDates requestDates);
   void generateManagedDates();
}
