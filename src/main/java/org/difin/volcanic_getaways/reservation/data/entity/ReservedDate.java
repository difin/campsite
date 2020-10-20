package org.difin.volcanic_getaways.reservation.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "RESERVED_DATE")
public class ReservedDate {

	@Getter
	@Setter
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserved_date_seq")
	@SequenceGenerator(name = "reserved_date_seq", sequenceName = "reserved_date_id_seq", allocationSize = 1)
	@Column(nullable = false, insertable = false, updatable = false)
	private int id;

	@Getter
	@Version
	private Integer version;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "MANAGED_DATE_ID", referencedColumnName = "ID", nullable = false)
	private ManagedDate managedDate;

	@Getter
	@Setter
	@ManyToOne
	@JoinColumn(name = "RESERVATION_ID", referencedColumnName = "ID", nullable = false)
	private Reservation reservation;
}
