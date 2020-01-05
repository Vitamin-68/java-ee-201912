package ua.ithillel.dnepr.common.persistence;

import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public interface AbstractQuery<IdType, EntityType extends BaseEntity<IdType>> {
    Root<IdType, EntityType> from(Class<? extends EntityType> entityClass);
//    select(Root<IdType, EntityType> entityRoot);
}
