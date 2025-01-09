package dev.eggl.persistance;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class IntervalStatusRepository implements PanacheRepositoryBase<IntervalStatus, Integer> {

    public List<Integer> findMissingEntriesForUsersAndDay(List<Integer> userIds, LocalDate date) {
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        // 1. Find all user IDs that DO have an interval_status entry for today
        TypedQuery<Integer> query = getEntityManager().createQuery(
                "SELECT DISTINCT i.userId FROM IntervalStatus i " +
                        "WHERE i.userId IN :userIds " +
                        "  AND i.createdAt >= :startOfDay " +
                        "  AND i.createdAt < :endOfDay",
                Integer.class);
        query.setParameter("userIds", userIds);
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);

        List<Integer> existingUserIds = query.getResultList();

        // 2. Create a new list for missing user IDs
        List<Integer> missingUserIds = new ArrayList<>(userIds);
        missingUserIds.removeAll(existingUserIds);

        // The remaining IDs in missingUserIds have no entry for today
        return missingUserIds;
    }
}