package adapters.outbound.jpa;

import adapters.outbound.jpa.entity.PriceLogEntity;
import application.port.PriceLogRepository;
import domain.model.PriceLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;


@ApplicationScoped
public class PriceLogRepositoryAdapter implements PriceLogRepository {

    @Inject
    PriceLogPanacheRepository priceLogRepo;

    @Override
    @Transactional
    public PriceLog savePriceLog(PriceLog log) {
        PriceLogEntity entity = PriceLogMapper.toEntity(log);
        priceLogRepo.persist(entity);
        priceLogRepo.flush();
        return PriceLogMapper.toDomain(entity);
    }

    @Override
    public Optional<PriceLog> findById(Integer logId) {
        return priceLogRepo.findByIdOptional(Long.valueOf(logId)).map(PriceLogMapper::toDomain);
    }




//
//    @Override
//    @Transactional
//    public List<PriceLog> findAllByItemId(String itemId) {
//        List<PriceLogEntity> entities = priceLogRepo.findAllByItemId(itemId);
//        return entities.stream()
//                .map(PriceLogMapper::toDomain)
//                .collect(Collectors.toList());
//    }
}
