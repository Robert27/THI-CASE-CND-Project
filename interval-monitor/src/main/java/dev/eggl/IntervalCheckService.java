package dev.eggl;

import dev.eggl.grpc.OderListClientService;
import dev.eggl.grpc.ObjectClientService;
import dev.eggl.grpc.UserClientService;
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

    @Inject
    OderListClientService orderClientService;

    @Scheduled(every = "10s")
    void checkMissingEntries() {
        // 1) Load user list
        userClientService.getUserIds()
                .subscribe().with(userIdsResponse -> {
                    List<Integer> userIds = userIdsResponse.userIds;
                    System.out.println("Received user IDs: " + userIds);

                    // 2) Run findMissingEntriesForUsersAndDay for today
                    List<Integer> missingUserIds = repository.findMissingEntriesForUsersAndDay(userIds,
                            LocalDate.now());
                    System.out.println("Missing user IDs for today's entries: " + missingUserIds);

                    // 3) Use ObjectClientService to fetch object IDs per batch of 5 users
                    for (int i = 0; i < missingUserIds.size(); i += 5) {
                        List<Integer> batch = missingUserIds.subList(i, Math.min(i + 5, missingUserIds.size()));
                        System.out.println("Fetching objects for batch: " + batch);
                        objectClientService.findAllDayUsers(5, batch)
                                .subscribe().with(dayUsersResponse -> {
                                    System.out.println("Received day users: " + dayUsersResponse);
                                    dayUsersResponse.userObjectIds.forEach(userObjectIds -> {
                                        System.out.println("User ID: " + userObjectIds.userId);
                                        System.out.println("Object IDs: " + userObjectIds.objectIds);

                                        // 4) Submit missing order for each user
              orderClientService
    .submitMissingOrder(userObjectIds.userId, userObjectIds.objectIds,
        LocalDate.now().toString())
                                                .subscribe().with(success -> {
                                                    if (success) {
                                                        System.out.println(
                                                                "Successfully submitted missing order for user ID: "
                                                                        + userObjectIds.userId);
                                                        repository.storeConfirmedUserId(userObjectIds.userId);
                                                    } else {
                                                        System.err
                                                                .println("Failed to submit missing order for user ID: "
                                                                        + userObjectIds.userId);
                                                    }
                                                });
                                    });
                                });
                    }
                });
    }
}
