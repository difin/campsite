package campsite.reservation.service;

import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.in.BookingDates;
import reactor.core.publisher.Flux;

public interface AvailabilityService {

   Flux<AvailableDateModel> getAvailableDates();
   Flux<AvailableDateModel> getAvailableDates(BookingDates availableDatesRange);
   void generateManagedDates();
}
