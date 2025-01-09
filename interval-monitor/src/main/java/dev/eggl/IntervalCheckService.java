package dev.eggl;

import dev.eggl.persistance.IntervalStatusRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class IntervalCheckService {

    @Inject
    IntervalStatusRepository repository;

    @Inject
    ObjectClientService objectClientService;

    @Scheduled(every = "5s")
    void checkMissingEntries() {
        List<Integer> userIds = List.of(1, 2, 3);
        List<Integer> missingUserIds = repository.findMissingEntriesForUsersAndDay(userIds, LocalDate.now());

        // For each batch of 5 missing user IDs, request the day users from the object service
        for (int i = 0; i < missingUserIds.size(); i += 5) {
            List<Integer> batch = missingUserIds.subList(i, Math.min(i + 5, missingUserIds.size()));
            objectClientService.findAllDayUsers(0, batch)
                    .subscribe().with(reply -> {
                        System.out.println("Received day users: " + reply);
                    });
        }
    }
}
