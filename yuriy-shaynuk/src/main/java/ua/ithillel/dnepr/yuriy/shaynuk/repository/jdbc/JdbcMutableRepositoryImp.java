package ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc;

import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.sql.Connection;

public class JdbcMutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseJdbcRepository<EntityType, IdType>
        implements MutableRepository<EntityType, IdType> {

    public JdbcMutableRepositoryImp(Connection connection, Class<? extends EntityType> clazz) {
        super(connection, clazz);
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
