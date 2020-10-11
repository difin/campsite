package campsite.reservation.data.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "RESERVED_DATE")
public class ReservedDate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserved_date_seq")
	@SequenceGenerator(name = "reserved_date_seq", sequenceName = "reserved_date_id_seq", allocationSize = 1)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MANAGED_DATE_ID", referencedColumnName = "ID", nullable = false)
	private ManagedDate managedDate;

	@Column(nullable = false)
	private int reservationId;
}
