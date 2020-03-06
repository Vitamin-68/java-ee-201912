package ua.ithillel.dnepr.tymoshenko.olga.jpa.jparepository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class jpaRepository <EntityType extends AbstractEntity<IdType>, IdType extends Serializable>
        implements CrudRepository<EntityType, IdType> {

    private final EntityManager entityManager;
    private final EntityTransaction transaction;
    private final Class<? extends EntityType> clazz;

   public jpaRepository(EntityManagerFactory entityManagerFactory, Class<? extends EntityType> clazz){
     entityManager=entityManagerFactory.createEntityManager();
      transaction = entityManager.getTransaction();
      this.clazz=clazz;
   }

    @Override
    public Optional<List<EntityType>> findAll() {
       List<EntityType> result=findAllEntity();
       return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        EntityType entity= findbyID(id);
        return entity==null?Optional.empty():Optional.of(entity);
   }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        Objects.requireNonNull(fieldName, "Field name is undefined");
        if (fieldName.isEmpty()) {
            log.error("Field name is empty");
            throw new IllegalArgumentException("Field name is empty");
        }
        boolean compare=false;
        Field[] listField=clazz.getDeclaredFields();
        for (Field field:listField) {
            if (field.getName().equals(fieldName)){
                compare=true;
                break;
            }
        }
        if(!compare){
            log.error("There is not this field in thid entity");
            throw new IllegalArgumentException("There is not this field in thid entity");
         }
       List<EntityType> list=new ArrayList<>();
        StringBuilder query=new StringBuilder();
        query.append("from ").append(clazz.getSimpleName())
                .append(" as p where p.")
                .append(fieldName)
                .append("=:field");
        List<EntityType> result = (List<EntityType>) entityManager.createQuery(query.toString())
                .setParameter("field", value)
                .getResultList();

        return result.isEmpty()?Optional.empty():Optional.of(result);
    }

    @Override
    public EntityType create(EntityType entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        EntityType entityType = null;
         if (!isEntityExist(entity)){
           transaction.begin();
            entityManager.persist(entity);
           transaction.commit();
        }else{
             log.error("Entity  exist");
         }
         return entity;
     }

    @Override
    public EntityType update(EntityType entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        transaction.begin();
        EntityType result=entityManager.merge(entity);
        transaction.commit();
        return result;
    }

    @Override
    public EntityType delete(IdType id) {
        EntityType entity= findbyID(id);
        Objects.requireNonNull(entity, "Entity is not exist");
            transaction.begin();
            entityManager.remove(entity);
            transaction.commit();
            return entity;
    }

    private  List<EntityType> findAllEntity(){
        final StringBuilder query=new StringBuilder();
        query.append("from ").append(clazz.getSimpleName());
        return entityManager.createQuery(query.toString()).getResultList();
      }

      private boolean isEntityExist(EntityType entity){
          List<EntityType> result=findAllEntity();
          if (!result.isEmpty()){
              for (EntityType e:result) {
                  if(e.equals(entity)) {
                      return true;
                  }
               }
           }
       return false;
      }

      private EntityType findbyID(IdType id){
           return entityManager.find(clazz, id);
      }
}
