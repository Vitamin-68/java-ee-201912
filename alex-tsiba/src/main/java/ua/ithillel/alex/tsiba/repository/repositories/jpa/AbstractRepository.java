package ua.ithillel.alex.tsiba.repository.repositories.jpa;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Slf4j
public abstract class AbstractRepository<EntityType extends AbstractEntity<Integer>> {
    final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit");
    final EntityManager entityManager = entityManagerFactory.createEntityManager();

    protected Class<EntityType> objectClass;

    public AbstractRepository(Class<EntityType> objectClass) {
        this.objectClass = objectClass;
    }
}
