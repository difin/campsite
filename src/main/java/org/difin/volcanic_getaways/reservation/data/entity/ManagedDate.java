package org.difin.volcanic_getaways.reservation.data.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "MANAGED_DATE")
public class ManagedDate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "managed_date_seq")
	@SequenceGenerator(name = "managed_date_seq", sequenceName = "managed_date_id_seq", allocationSize = 1)
	@Column(nullable = false, insertable = false, updatable = false)
	private int id;

	@Column(nullable = false)
	private LocalDate date;

	@Version
	private Integer version;

	@OneToMany(mappedBy = "managedDate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ReservedDate> reservedDates;
}
