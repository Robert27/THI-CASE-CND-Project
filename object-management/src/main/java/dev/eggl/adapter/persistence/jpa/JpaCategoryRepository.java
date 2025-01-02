package dev.eggl.adapter.persistence.jpa;

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
    public void save(Category category) {
        panacheRepository.getEntityManager().merge(CategoryMapper.toJpaEntity(category));
    }

    @Override
    public List<Category> findAll() {
       List<CategoryJpaEntity> entities = panacheRepository.findAll().list();
         return CategoryMapper.toDomainList(entities);
    }

    @Override
    public void delete(Long id) {
        panacheRepository.deleteById(id.toString());
    }
}
