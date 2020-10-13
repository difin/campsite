package campsite.reservation;

import campsite.reservation.service.ManagedDatesFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

@Component
@Order(0)
public class CampsiteApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    final ManagedDatesFacade managedDatesFacade;

    @Autowired
    public CampsiteApplicationListener(ManagedDatesFacade managedDatesFacade) {
        this.managedDatesFacade = managedDatesFacade;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        managedDatesFacade.generateManagedDates();

        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }
}
