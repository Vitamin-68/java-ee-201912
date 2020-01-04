package ua.ithillel.dnepr.common.persistence;

import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public interface CriteriaBuilder<IdType, EntityType extends BaseEntity<IdType>> {
    CriteriaQuery<IdType, EntityType> createQuery(Class<? extends EntityType> entityClass);
}
