package adapters.outbound.jpa;

import adapters.outbound.jpa.entity.PriceLogEntity;
import domain.model.PriceLog;

public class PriceLogMapper {

    public static PriceLogEntity toEntity(PriceLog log) {
        PriceLogEntity entity = new PriceLogEntity();
        // entity.id = ... => wird autogeneriert
        entity.itemId = log.getItemId();
        entity.price = log.getPrice();
        entity.available = log.getAvailability();
        entity.checkedAt = log.getCheckedAt();
        return entity;
    }

    public static PriceLog toDomain(PriceLogEntity entity) {
        return new PriceLog(
                entity.id,
                entity.itemId,
                entity.price,
                entity.available,
                entity.checkedAt
        );
    }
}
