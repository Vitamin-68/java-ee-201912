package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JpaCrudRepository<EntityType extends AbstractEntity<IdType>, IdType> implements CrudRepository<EntityType, IdType> {
    private final JpaImmutableRepository<EntityType, IdType> immutableRepository;
    private final JpaMutableRepository<EntityType, IdType> mutableRepository;

    private static <T> List<T> loadAllData(Class<T> type, Session session) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(type);
        criteria.from(type);
        return session.createQuery(criteria).getResultList();
    }

    public JpaCrudRepository() {
        immutableRepository = new JpaImmutableRepository<>();
        mutableRepository = new JpaMutableRepository<>();

        //Session session = HibernateUtils.getSession();
        //List<EntityType> users = loadAllData(EntityType.class, session);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return Optional.empty();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public EntityType create(EntityType entity) {
        return null;
    }

    @Override
    public EntityType update(EntityType entity) {
        return null;
    }

    @Override
    public EntityType delete(IdType id) {
        return null;
    }
}
