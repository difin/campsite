package campsite.reservation.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "RESERVATION")
public class Reservation {

	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq")
	@SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_id_seq", allocationSize = 1)
	@Column(nullable = false, insertable = false, updatable = false)
	private int id;

	@Getter
	@Setter
	@Column(nullable = false)
	private String name;

	@Getter
	@Setter
	@Column(nullable = false)
	private String email;

	@Column(name = "BOOKING_REF", updatable = false, nullable = false)
	@Getter
	@Setter
	private UUID bookingRef;

	@Getter
	@OneToMany(mappedBy = "reservationId", fetch = FetchType.LAZY)
	List<ReservedDate> reservedDates;
}
