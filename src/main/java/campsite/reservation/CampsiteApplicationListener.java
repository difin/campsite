package campsite.reservation;

import campsite.reservation.service.AvailabilityFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
@Order(0)
public class CampsiteApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    final AvailabilityFacade availabilityFacade;

    @Autowired
    public CampsiteApplicationListener(AvailabilityFacade availabilityFacade) {
        this.availabilityFacade = availabilityFacade;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        availabilityFacade.generateManagedDates();

        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }
}
