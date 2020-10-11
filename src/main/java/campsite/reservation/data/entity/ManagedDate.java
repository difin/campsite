package campsite.reservation.data.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "MANAGED_DATE")
public class ManagedDate {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "managed_date_seq")
	@SequenceGenerator(name = "managed_date_seq", sequenceName = "managed_date_id_seq", allocationSize = 1)
	private int id;

	@Column(nullable = false)
	private LocalDate date;

	@OneToMany(mappedBy = "managedDate", fetch = FetchType.LAZY)
	List<ReservedDate> reservedDates;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public List<ReservedDate> getReservedDates() {
		return reservedDates;
	}

	public void setReservedDates(List<ReservedDate> reservedDates) {
		this.reservedDates = reservedDates;
	}
}
