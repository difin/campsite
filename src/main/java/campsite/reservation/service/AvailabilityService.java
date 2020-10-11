package campsite.reservation.service;

import campsite.reservation.data.repository.ManagedDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final ManagedDateRepository managedDateRepository;

    @Autowired
    public AvailabilityService(ManagedDateRepository managedDateRepository) {
        this.managedDateRepository = managedDateRepository;
    }

   public List<LocalDate> getAvailableDates(){
       return managedDateRepository.getAvailableDates().stream().map(m -> m.getDate()).collect(Collectors.toList());
    }
}
