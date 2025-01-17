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

    public List<Integer> findMissingObjectsForUserAndDay(Integer userId, List<Integer> objectIds, LocalDate date) {
        Instant startOfDay = date.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endOfDay = date.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();

        TypedQuery<Integer> query = getEntityManager().createQuery(
                "SELECT DISTINCT i.objectId FROM IntervalStatus i " +
                        "WHERE i.objectId IN :objectIds " +
                        "  AND i.createdAt >= :startOfDay " +
                        "  AND i.createdAt < :endOfDay",
                Integer.class);
        query.setParameter("objectIds", objectIds);
        query.setParameter("startOfDay", Date.from(startOfDay));
        query.setParameter("endOfDay", Date.from(endOfDay));

        List<Integer> existingObjectIds = query.getResultList();
        List<Integer> missingObjectIds = new ArrayList<>(objectIds);
        missingObjectIds.removeAll(existingObjectIds);

        return missingObjectIds;
    }

    @Transactional
    public void storeConfirmedObjectId(int objectId, Date date) {
        IntervalStatus intervalStatus = new IntervalStatus();
        intervalStatus.setObjectId(objectId);
        intervalStatus.setCreatedAt(date);
        persist(intervalStatus);
    }
}
