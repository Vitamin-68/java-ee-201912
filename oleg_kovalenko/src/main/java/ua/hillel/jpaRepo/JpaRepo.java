package ua.hillel.jpaRepo;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ua.hillel.jpa_entity.RegionJpa;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class JpaRepo<EntityType extends AbstractEntity<Integer>>
        implements CrudRepository<EntityType, Integer> {

    private Class<EntityType> clazz;
    private EntityManager manager;
    private EntityTransaction transaction;


    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistence-config");
        EntityManager manager = factory.createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        Class<?> clazz = RegionJpa.class;
        JpaRepo cityJpa = new JpaRepo(clazz, manager, transaction);
        System.out.println(cityJpa.findById(4312));
//        cityJpa.findByField("city_id",4400);

//        System.out.println(cityJpa.findByField("city_Id", 4400));
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        List<EntityType> result = manager.createQuery("from " + clazz.getSimpleName(), clazz).getResultList();
        return Optional.of(result);
    }

    @Override
    public Optional<EntityType> findById(Integer id) {
        EntityType entity = manager.find(clazz, id);
        return Optional.of(entity);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<EntityType> entity = cb.createQuery(clazz);
        Root<EntityType> root = entity.from(clazz);
        entity.select(root);
//        entity.where(entity.equals(root.get(fieldName),"cc"));
        manager.createQuery(entity).getResultList().forEach(System.out::println);


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
    public EntityType delete(Integer id) {
        return null;
    }
}