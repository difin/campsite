package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.model.response.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ManagedDatesFacade {

   Flux<AvailableDateModel> getAvailableDatesReactive(RequestDates requestDates);

   List<ManagedDate> getAvailableDatesBlocking(RequestDates requestDates);
   List<ManagedDate> lockDatesBlocking(RequestDates requestDates);
   void generateManagedDatesBlocking();
}
