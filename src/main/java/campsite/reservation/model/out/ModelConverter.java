package campsite.reservation.model.out;

import campsite.reservation.data.entity.ManagedDate;
import org.springframework.stereotype.Component;

@Component
public class ModelConverter {

    public AvailableDateModel managedDateEntityToDTO(ManagedDate managedDate) {
        return new AvailableDateModel(managedDate.getDate());
    }
}
