package org.difin.volcanic_getaways.reservation.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "RESERVATION")
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq")
	@SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_id_seq", allocationSize = 1)
	@Column(nullable = false, insertable = false, updatable = false)
	private int id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(name = "BOOKING_REF", updatable = false, nullable = false)
	private String bookingRef;

	@Version
	private Integer version;

	@OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ReservedDate> reservedDates;
}
