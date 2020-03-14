package vitaly.mosin.spring.data.jpa.repository;

import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

public class SpringEntityRepository implements CrudRepository {
    @Override
    public Optional<List> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional findById(Object id) {
        return Optional.empty();
    }

    @Override
    public Optional<List> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public BaseEntity create(BaseEntity entity) {
        return null;
    }

    @Override
    public BaseEntity update(BaseEntity entity) {
        return null;
    }

    @Override
    public BaseEntity delete(Object id) {
        return null;
    }
}
