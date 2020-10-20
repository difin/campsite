package org.difin.volcanic_getaways.reservation.data.repository;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;

import java.time.LocalDate;
import java.util.List;

public interface CustomManagedDateRepository {

    List<ManagedDate> getAvailableDates(int spotsNum, LocalDate arrival, LocalDate departure, boolean lock);
}
