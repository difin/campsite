package campsite.reservation.data.repository;

import campsite.reservation.data.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r                                " +
            "from Reservation r                     " +
            "join FETCH r.reservedDates             " +
            "where r.bookingRef = :bookingReference ")
    Reservation findByBookingRef(String bookingReference);

    @Query("select distinct r              " +
            "from Reservation r            " +
            "join FETCH r.reservedDates rd " +
            "join FETCH rd.managedDate md  " +
            "where md.date >= :arrival     " +
            "and md.date <= :departure     ")
    List<Reservation> findReservationsForDates(LocalDate arrival, LocalDate departure);
}
