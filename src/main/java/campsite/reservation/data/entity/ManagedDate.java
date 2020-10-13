package campsite.reservation.data.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MANAGED_DATE")
public class ManagedDate {

	@Id
	@Getter
	@Setter
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "managed_date_seq")
	@SequenceGenerator(name = "managed_date_seq", sequenceName = "managed_date_id_seq", allocationSize = 1)
	@Column(nullable = false, insertable = false, updatable = false)
	private int id;

	@Getter
	@Setter
	@Column(nullable = false)
	private LocalDate date;

	@Getter
	@OneToMany(mappedBy = "managedDate", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	List<ReservedDate> reservedDates;
}
