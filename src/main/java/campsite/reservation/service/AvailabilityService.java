package campsite.reservation.service;

import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface AvailabilityService {

   Flux<LocalDate> getAvailableDates();
   Flux<LocalDate> getAvailableDates(LocalDate startDate, LocalDate endDate);
}
