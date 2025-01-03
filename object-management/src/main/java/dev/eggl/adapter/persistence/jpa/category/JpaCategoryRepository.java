package dev.eggl.adapter.persistence.jpa.category;

import dev.eggl.domain.model.Category;
import dev.eggl.port.out.CategoryPort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class JpaCategoryRepository implements CategoryPort {
    private final JpaCategoryPanacheRepository panacheRepository;


    public JpaCategoryRepository(JpaCategoryPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public List<Category> findAll() {
        List<CategoryJpaEntity> entities = panacheRepository.findAll().list();
        return CategoryMapper.toDomainList(entities);
    }

    @Override
    public Boolean existsById(Integer id) {
        return panacheRepository.count("id", id) > 0;
    }
}
