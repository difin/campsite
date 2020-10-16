package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.model.out.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.model.in.RequestDates;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface ManagedDatesFacade {

   Flux<AvailableDateModel> getAvailableDates(Optional<RequestDates> requestDates);
   List<ManagedDate> getAvailableDatesBlocking(Optional<RequestDates> requestDates);
   List<ManagedDate> lockDates(RequestDates requestDates);
   void generateManagedDates();
}
