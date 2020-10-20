package org.difin.volcanic_getaways.reservation.data.repository;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ManagedDateRepository extends JpaRepository<ManagedDate, Integer> {

    @Query("select md                  " +
            "from ManagedDate md       " +
            "where md.date >= :arrival " +
            "and md.date <= :departure ")
    List<ManagedDate> getManagedDates(LocalDate arrival, LocalDate departure);
}