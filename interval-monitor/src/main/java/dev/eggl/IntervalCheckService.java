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

    @Inject
    UserClientService userClientService;

    @Scheduled(every = "10s")
    void checkMissingEntries() {
        userClientService.getUserIds()
                .subscribe().with(userIdsResponse -> {
                    List<Integer> userIds = userIdsResponse.userIds;
                    System.out.println("Received user IDs: " + userIds);

                    // Process in batches of 5
                    for (int i = 0; i < userIds.size(); i += 5) {
                        List<Integer> batch = userIds.subList(i, Math.min(i + 5, userIds.size()));
                        objectClientService.findAllDayUsers(0, batch)
                                .subscribe().with(dayUsersResponse -> {
                                    System.out.println("Received day users: " + dayUsersResponse);

                                    // Fix print logic: iterate over each UserObjectIds
                                    for (ObjectClientService.UserObjectIds userObjectIds : dayUsersResponse.userObjectIds) {
                                        System.out.println("Current user ID: " + userObjectIds.userId);
                                        System.out.println("Current user object IDs: " + userObjectIds.objectIds);
                                    }
                                });
                    }
                    List<Integer> missingUserIds = repository.findMissingEntriesForUsersAndDay(userIds,
                            LocalDate.now());

                    // For each missing user, call ObjectClientService to fetch items
                    for (Integer userId : missingUserIds) {
                        System.out.println("Fetching items for user ID " + userId);
                    }
                });
    }
}
