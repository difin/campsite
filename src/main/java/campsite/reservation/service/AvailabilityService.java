package campsite.reservation.service;

import campsite.reservation.model.AvailableDateModel;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface AvailabilityService {

   Flux<AvailableDateModel> getAvailableDates();
   Flux<AvailableDateModel> getAvailableDates(LocalDate startDate, LocalDate endDate);
}
