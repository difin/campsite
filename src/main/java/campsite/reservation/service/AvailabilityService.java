package campsite.reservation.service;

import campsite.reservation.model.out.AvailableDateModel;
import campsite.reservation.model.in.RequestedDatesRange;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

public interface AvailabilityService {

   Flux<AvailableDateModel> getAvailableDates();
   Flux<AvailableDateModel> getAvailableDates(@Valid RequestedDatesRange availableDatesRange);
}
