package org.difin.volcanic_getaways.reservation.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	private String bookingRef;

	@Getter
	@OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private List<ReservedDate> reservedDates;
}
