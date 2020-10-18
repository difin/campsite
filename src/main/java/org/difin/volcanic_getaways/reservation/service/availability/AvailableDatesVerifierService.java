package org.difin.volcanic_getaways.reservation.service.availability;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.model.request.RequestDates;
import org.difin.volcanic_getaways.reservation.model.response.AvailableDateModel;
import reactor.core.publisher.Flux;

import java.util.List;

public interface AvailableDatesVerifierService {

    Flux<AvailableDateModel> getAvailableDatesReactive(RequestDates requestDates);
    List<ManagedDate> getAvailableDatesBlocking(RequestDates requestDates);
    List<ManagedDate> lockDatesBlocking(RequestDates requestDates);
}
