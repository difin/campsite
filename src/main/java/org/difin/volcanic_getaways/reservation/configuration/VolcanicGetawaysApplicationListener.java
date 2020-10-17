package org.difin.volcanic_getaways.reservation.configuration;

import org.difin.volcanic_getaways.reservation.service.ManagedDatesFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Order(0)
public class VolcanicGetawaysApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    final ManagedDatesFacade managedDatesFacade;

    @Autowired
    public VolcanicGetawaysApplicationListener(ManagedDatesFacade managedDatesFacade) {
        this.managedDatesFacade = managedDatesFacade;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        Locale.setDefault(Locale.ENGLISH);

        managedDatesFacade.generateManagedDates();
    }
}
