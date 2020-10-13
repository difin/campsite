package campsite.reservation.data.repository;

import campsite.reservation.data.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r                                " +
            "from Reservation r                     " +
            "join FETCH r.reservedDates             " +
            "where r.bookingRef = :bookingReference ")
    Reservation findByBookingRef(String bookingReference);
}
