package campsite.reservation.data.repository;

import campsite.reservation.data.entity.ManagedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagedDateRepository extends JpaRepository<ManagedDate, Integer> {

    @Query("select md                       " +
           "from ManagedDate md             " +
           "where md.reservedDates.size < :spotsNum "
    )
    List<ManagedDate> getAvailableDates(int spotsNum);
}
