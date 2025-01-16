package dev.eggl.persistance;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class IntervalStatusRepository implements PanacheRepositoryBase<IntervalStatus, Integer> {

    public List<Integer> findMissingEntriesForUsersAndDay(List<Integer> userIds, LocalDate date) {
        // Convert the given LocalDate to a start/end Instant for the day
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // Find all userIds that DO have an entry for the given LocalDate
        TypedQuery<Integer> query = getEntityManager().createQuery(
                "SELECT DISTINCT i.userId FROM IntervalStatus i " +
                        "WHERE i.userId IN :userIds " +
                        "  AND i.createdAt >= :startOfDay " +
                        "  AND i.createdAt < :endOfDay",
                Integer.class);
        query.setParameter("userIds", userIds);
        query.setParameter("startOfDay", Date.from(startOfDay));
        query.setParameter("endOfDay", Date.from(endOfDay));

        List<Integer> existingUserIds = query.getResultList();

        // Create a new list for missing user IDs
        List<Integer> missingUserIds = new ArrayList<>(userIds);
        missingUserIds.removeAll(existingUserIds);

        return missingUserIds;
    }

    @Transactional
    public void storeConfirmedUserId(int userId) {
        IntervalStatus intervalStatus = new IntervalStatus();
        intervalStatus.setUserId(userId);
        intervalStatus.setCreatedAt(new Date());
        persist(intervalStatus);
    }
}
