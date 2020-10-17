package org.difin.volcanic_getaways.reservation.service.availability;

import org.difin.volcanic_getaways.reservation.data.entity.ManagedDate;
import org.difin.volcanic_getaways.reservation.data.repository.ManagedDateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class ManagedDatesCreationBatchServiceImpl implements ManagedDatesCreationBatchService {

    private ManagedDateRepository managedDateRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ManagedDatesCreationBatchServiceImpl(ManagedDateRepository managedDateRepository) {
        this.managedDateRepository = managedDateRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void generateManagedDates() {

        LOGGER.debug("generateManagedDates - enter");

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
        List<LocalDate> datesToCreate = allDates;

        datesToCreate
                .forEach(t -> {
                    ManagedDate managedDate = new ManagedDate();
                    managedDate.setDate(t);
                    managedDateRepository.save(managedDate);
                });

        LOGGER.debug("generateManagedDates - exit; new managed dates added=" + datesToCreate.size());
    }
}
