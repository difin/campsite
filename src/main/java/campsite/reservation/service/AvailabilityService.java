package campsite.reservation.service;

import campsite.reservation.data.repository.ManagedDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailabilityService {

    private final ManagedDateRepository managedDateRepository;
    private int spotsNum;

    @Autowired
    public AvailabilityService(ManagedDateRepository managedDateRepository,
                               @Value("${app.spots-num}") int spotsNum) {

        this.managedDateRepository = managedDateRepository;
        this.spotsNum = spotsNum;
    }

   public List<LocalDate> getAvailableDates(){
       return managedDateRepository.getAvailableDates(spotsNum).stream().map(m -> m.getDate()).collect(Collectors.toList());
    }
}
