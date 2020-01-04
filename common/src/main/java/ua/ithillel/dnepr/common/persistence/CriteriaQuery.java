package ua.ithillel.dnepr.common.persistence;

import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

public interface CriteriaQuery<IdType, EntityType extends BaseEntity<IdType>>  extends AbstractQuery<IdType, EntityType>{
    //select(Root<IdType, EntityType> root);
}
