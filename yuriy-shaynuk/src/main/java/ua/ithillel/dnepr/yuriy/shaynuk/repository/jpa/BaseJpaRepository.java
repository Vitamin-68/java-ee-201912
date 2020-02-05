package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import javax.persistence.EntityManager;

public abstract class BaseJpaRepository <EntityType extends AbstractEntity<IdType>, IdType>{
    protected EntityManager entityManager;
    protected Class<EntityType> clazz;
}
