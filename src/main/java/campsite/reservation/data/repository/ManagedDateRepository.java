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

    String GET_AVAILABLE_DATES = "select md                               " +
                                 "from ManagedDate md                     " +
                                 "where md.reservedDates.size < :spotsNum " +
                                 "and md.date >= :arrival                 " +
                                 "and md.date <= :departure               ";

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(GET_AVAILABLE_DATES)
    List<ManagedDate> getAvailableDatesLocking(int spotsNum, LocalDate arrival, LocalDate departure);

    @Lock(LockModeType.NONE)
    @Query(GET_AVAILABLE_DATES)
    List<ManagedDate> getAvailableDatesNotLocking(int spotsNum, LocalDate arrival, LocalDate departure);

    @Query("select md                 " +
           "from ManagedDate md       " +
           "where md.date >= :arrival " +
           "and md.date <= :departure "
    )
    List<ManagedDate> getManagedDates(LocalDate arrival, LocalDate departure);

}
