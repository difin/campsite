package org.difin.volcanic_getaways.reservation.data.repository;

import org.difin.volcanic_getaways.reservation.data.entity.ReservedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservedDateRepository extends JpaRepository<ReservedDate, Integer> {
}
