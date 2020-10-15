package campsite.reservation.data.repository;

import campsite.reservation.data.entity.ManagedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ManagedDateRepository extends JpaRepository<ManagedDate, Integer> {

    String SELECT_DATES_RANGE = "select md                 " +
                                "from ManagedDate md       " +
                                "where md.date >= :arrival " +
                                "and md.date <= :departure ";

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(SELECT_DATES_RANGE)
    List<ManagedDate> lockDates(LocalDate arrival, LocalDate departure);

    @Query(SELECT_DATES_RANGE)
    List<ManagedDate> getManagedDates(LocalDate arrival, LocalDate departure);

    @Query("select md                                " +
            "from ManagedDate md                     " +
            "where md.reservedDates.size < :spotsNum " +
            "and md.date >= :arrival                 " +
            "and md.date <= :departure               ")
    List<ManagedDate> getAvailableDates(int spotsNum, LocalDate arrival, LocalDate departure);
}
