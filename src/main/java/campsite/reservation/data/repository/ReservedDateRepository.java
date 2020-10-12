package campsite.reservation.data.repository;

import campsite.reservation.data.entity.ReservedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservedDateRepository extends JpaRepository<ReservedDate, Integer> {
}
