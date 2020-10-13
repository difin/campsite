package campsite.reservation;

import campsite.reservation.service.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
@Order(0)
public class CampsiteApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    final AvailabilityService availabilityService;

    @Autowired
    public CampsiteApplicationListener(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        availabilityService.generateManagedDates();

        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }
}
