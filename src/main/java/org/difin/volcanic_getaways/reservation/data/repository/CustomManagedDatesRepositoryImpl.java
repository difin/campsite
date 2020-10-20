package org.difin.volcanic_getaways.reservation.data.repository;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
public class CustomManagedDatesRepositoryImpl implements CustomManagedDateRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<ManagedDate> getAvailableDates(int spotsNum, LocalDate arrival, LocalDate departure, boolean lock) {

        String queryText = "select md                               " +
                           "from ManagedDate md                     " +
                           "where md.reservedDates.size < :spotsNum " +
                           "and md.date >= :arrival                 " +
                           "and md.date <= :departure               ";

        Query query =
            entityManager
                .createQuery(queryText)
                .setParameter("spotsNum", spotsNum)
                .setParameter("arrival", arrival)
                .setParameter("departure", departure);

        query.setHint("javax.persistence.lock.timeout", 2000);
        query.setHint("javax.persistence.query.timeout", 2000);

        if (lock)
            query.setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        return query.getResultList();
    }

}
