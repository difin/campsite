package org.difin.volcanic_getaways.reservation.data.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "RESERVED_DATE")
public class ReservedDate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserved_date_seq")
	@SequenceGenerator(name = "reserved_date_seq", sequenceName = "reserved_date_id_seq", allocationSize = 1)
	@Column(nullable = false, insertable = false, updatable = false)
	private int id;

	@Version
	private Integer version;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MANAGED_DATE_ID", referencedColumnName = "ID", nullable = false)
	private ManagedDate managedDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RESERVATION_ID", referencedColumnName = "ID", nullable = false)
	private Reservation reservation;
}
