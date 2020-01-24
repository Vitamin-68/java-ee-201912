package ua.ithillel.dnepr.common.persistence;

import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public interface EntityManager {
    <IdType, EntityType extends BaseEntity<IdType>> CriteriaBuilder<IdType, EntityType> getCriteriaBuilder();

    Transaction getTransaction();
}
