package ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa;

import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public abstract class BaseJpaRepository <EntityType extends AbstractEntity<IdType>, IdType>{
    protected EntityManager entityManager;
    protected Class<EntityType> clazz;
    //protected CriteriaQuery<EntityType> criteria;
}
