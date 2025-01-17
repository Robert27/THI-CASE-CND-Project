package dev.eggl;

import dev.eggl.grpc.ObjectClientService;
import dev.eggl.grpc.OderListClientService;
import dev.eggl.grpc.UserClientService;
import dev.eggl.persistance.IntervalStatusRepository;
import dev.eggl.config.MockConfiguration;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;
import java.util.Date;
import java.time.ZoneId;

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

    @Inject
    MockConfiguration mockConfig;

    @Scheduled(every = "15s")
    void checkMissingEntriesMock() {
        if (mockConfig.isMockEnabled()) {
            performCheck();
        }
    }

    @Scheduled(every = "4h")
    void checkMissingEntriesProd() {
        if (!mockConfig.isMockEnabled()) {
            performCheck();
        }
    }

    private void performCheck() {
        LocalDate currentDate = mockConfig.isMockEnabled() ? mockConfig.getMockDate() : LocalDate.now();
        System.out.println("=== Starting interval check at " + currentDate + " ===");
        int weekday = (currentDate.getDayOfWeek().getValue() - 1) % 7;
        System.out.println("Processing for weekday: " + weekday);

        userClientService.getUserIds()
                .subscribe().with(userIdsResponse -> {
                    List<Integer> userIds = userIdsResponse.userIds;
                    System.out.println("Retrieved " + userIds.size() + " user IDs for processing");

                    for (int i = 0; i < userIds.size(); i += 5) {
                        List<Integer> batch = userIds.subList(i, Math.min(i + 5, userIds.size()));
                        System.out.println("Processing batch " + (i / 5 + 1) + " of " + Math.ceil(userIds.size() / 5.0)
                                + ": " + batch);

                        objectClientService.findAllDayUsers(weekday, batch)
                                .subscribe().with(dayUsersResponse -> {
                                    dayUsersResponse.userObjectIds.forEach(userObjectIds -> {
                                        System.out.println("Checking user " + userObjectIds.userId + " with "
                                                + userObjectIds.objectIds.size() + " objects");
                                        List<Integer> missingObjectIds = repository.findMissingObjectsForUserAndDay(
                                                userObjectIds.userId,
                                                userObjectIds.objectIds,
                                                currentDate);

                                        if (!missingObjectIds.isEmpty()) {
                                            System.out.println(
                                                    "Found " + missingObjectIds.size() + " missing objects for user "
                                                            + userObjectIds.userId + ": " + missingObjectIds);
                                            orderClientService.submitMissingOrder(
                                                    userObjectIds.userId,
                                                    missingObjectIds,
                                                    currentDate.toString())
                                                    .subscribe().with(success -> {
                                                        if (success) {
                                                            System.out.println(
                                                                    "✓ Successfully processed missing order for user "
                                                                            + userObjectIds.userId +
                                                                            " (Objects: " + missingObjectIds + ")");
                                                            missingObjectIds.forEach(objectId -> {
                                                                Date date = Date.from(
                                                                        currentDate.atStartOfDay(ZoneId.systemDefault())
                                                                                .toInstant());
                                                                repository.storeConfirmedObjectId(objectId, date);
                                                                System.out
                                                                        .println("  → Stored confirmation for object: "
                                                                                + objectId);
                                                            });
                                                        } else {
                                                            System.err.println(
                                                                    "✗ Failed to process missing order for user "
                                                                            + userObjectIds.userId +
                                                                            " (Objects: " + missingObjectIds + ")");
                                                        }
                                                    });
                                        } else {
                                            System.out.println(
                                                    "No missing objects found for user " + userObjectIds.userId);
                                        }
                                    });
                                });
                    }
                });
    }
}
