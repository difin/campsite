package campsite.reservation.data.repository;

import campsite.reservation.data.entity.ManagedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManagedDateRepository extends JpaRepository<ManagedDate, Integer> {

    @Query("select md                       " +
           "from ManagedDate md             " +
           "where md.reservedDates.size < 3 "
    )
    List<ManagedDate> getAvailableDates();
}
