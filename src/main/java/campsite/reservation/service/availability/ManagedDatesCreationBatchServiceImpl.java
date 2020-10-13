package campsite.reservation.service.availability;

import campsite.reservation.data.entity.ManagedDate;
import campsite.reservation.data.repository.ManagedDateRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class ManagedDatesCreationBatchServiceImpl implements ManagedDatesCreationBatchService {

    private final ManagedDateRepository managedDateRepository;

    public ManagedDatesCreationBatchServiceImpl(ManagedDateRepository managedDateRepository) {
        this.managedDateRepository = managedDateRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void generateManagedDates() {

        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusMonths(1);

        List<LocalDate> existingDates =
                managedDateRepository
                        .getManagedDates(start, end)
                        .stream()
                        .map(ManagedDate::getDate)
                        .collect(Collectors.toList());

        List<LocalDate> allDates =
                Stream.iterate(start, date -> date.plusDays(1))
                        .limit(DAYS.between(start, end))
                        .collect(Collectors.toList());

        allDates.removeAll(existingDates);

        allDates
                .forEach(t -> {
                    ManagedDate managedDate = new ManagedDate();
                    managedDate.setDate(t);
                    managedDateRepository.save(managedDate);
                });
    }
}
