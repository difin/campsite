package campsite.reservation.data.repository;

import campsite.reservation.data.entity.ManagedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ManagedDateRepository extends JpaRepository<ManagedDate, Integer> {

    @Query("select md                       " +
           "from ManagedDate md             " +
           "where md.reservedDates.size < :spotsNum " +
           "and md.date >= :startDate       " +
           "and md.date <= :endDate         "
    )
    List<ManagedDate> getAvailableDates(int spotsNum, LocalDate startDate, LocalDate endDate);
}
