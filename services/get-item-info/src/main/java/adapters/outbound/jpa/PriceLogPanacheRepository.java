package adapters.outbound.jpa;

import adapters.outbound.jpa.entity.PriceLogEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PriceLogPanacheRepository implements PanacheRepository<PriceLogEntity> {



//    public List<PriceLogEntity> findAllByItemId(String itemId) {
//        return list("itemId", itemId);
//    }
}
