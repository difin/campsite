package org.difin.volcanic_getaways.reservation.service.availability;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.model.in.RequestDates;
import org.difin.volcanic_getaways.reservation.model.out.AvailableDateModel;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

public interface AvailableDatesVerifierService {

    Flux<AvailableDateModel> getAvailableDates(Optional<RequestDates> requestDates);
    List<ManagedDate> getAvailableDatesBlocking(Optional<RequestDates> requestDates);
    List<ManagedDate> lockDates(RequestDates requestDates);
}
