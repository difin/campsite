package campsite.reservation.service;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.in.RequestDates;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AvailabilityService {

   Flux<AvailableDateModel> getAvailableDates();
   Flux<AvailableDateModel> getAvailableDates(RequestDates requestDates);
   void generateManagedDates();
   List<ManagedDate> getAvailableDatesEagerLocking(RequestDates requestDates);
   List<ManagedDate> getAvailableDatesEagerNotLocking(RequestDates requestDates);
}
