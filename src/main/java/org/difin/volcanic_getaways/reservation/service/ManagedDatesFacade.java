package org.difin.volcanic_getaways.reservation.service;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.model.response.AvailableDateModel;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ManagedDatesFacade {

   Flux<AvailableDateModel> getAvailableDates(RequestDates requestDates);
   List<ManagedDate> getAvailableDatesBlocking(RequestDates requestDates);
   List<ManagedDate> lockDates(RequestDates requestDates);
   void generateManagedDates();
}
