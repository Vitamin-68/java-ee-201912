package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa;

import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

public class JpaMutableRepository<EntityType extends AbstractEntity<IdType>, IdType> implements MutableRepository<EntityType, IdType> {
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
