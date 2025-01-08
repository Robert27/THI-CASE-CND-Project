package dev.eggl;
import dev.eggl.IntervalEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IntervalRepository implements PanacheRepositoryBase<IntervalEntity, Integer> {

    public IntervalEntity findByName(String name) {
        return find("name", name).firstResult();
    }
}
