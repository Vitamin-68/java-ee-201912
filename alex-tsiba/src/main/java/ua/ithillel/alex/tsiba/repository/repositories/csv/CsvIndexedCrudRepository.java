package ua.ithillel.alex.tsiba.repository.repositories.csv;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.alex.tsiba.repository.annotations.Column;
import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.alex.tsiba.repository.stores.DataStore;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class CsvIndexedCrudRepository<EntityType extends AbstractEntity<Integer>> extends AbstractRepository<EntityType>
        implements IndexedCrudRepository<EntityType, Integer> {

    private Map<String, Map<String, List<Integer>>> indexes = new HashMap();


    public CsvIndexedCrudRepository(DataStore dataStore) {
        super(dataStore);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        Optional result = Optional.empty();
        if(data.size() > 0){
            List items = new ArrayList();

            for (EntityType item : data.values()) {
                items.add(item);
            }

            result = Optional.of(items);
        }

        return result;
    }

    @Override
    public Optional<EntityType> findById(Integer id) {
        Optional result = Optional.empty();
        if (data.containsKey(id)) {
            EntityType item = cloneItem(data.get(id));
            result = Optional.of(item);
        }
        return result;
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        if (!dataStore.fieldExist(fieldName)) {
            throw new IllegalArgumentException("Field doesn't exist in object");
        }
        List result = new ArrayList();
        if(indexes.containsKey(fieldName)){
            Map index = indexes.get(fieldName);
            String uuid = UUID.nameUUIDFromBytes(String.valueOf(value).getBytes()).toString();
            if(index.containsKey(uuid)){
                List<Integer> itemsIds = (List) index.get(uuid);
                for (Integer id: itemsIds) {
                    EntityType item = data.get(id);
                    if(item != null){
                        result.add(item);
                    }
                }
            }
        }else{
            for (EntityType item : data.values()) {
                try {
                    Field field = item.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    if (value.equals(field.get(item))) {
                        result.add(item);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error("Field not found or not read!", e);
                }
            }
        }
        return Optional.of(result);
    }

    @Override
    public EntityType create(EntityType entity) {
        EntityType result = null;
        Integer id = entity.getId();
        if (id != -1 && data.containsKey(id)) {
            throw new IllegalArgumentException("Entity already exists");
        } else {
            if(id != -1){
                entity.setId(currentID++);
            }else{
                if(id > currentID){
                    currentID = id;
                }
            }
            data.put(entity.getId(), entity);
            indexes.forEach((key, items) -> {
                Field field = getFieldByName(dataStore.getObjClass(), key);
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    addToIndex(key, String.valueOf(value), entity.getId());
                } catch (IllegalAccessException e) {
                    log.error("Object didn't add to index", e);
                }
            });
            try {
                dataStore.save(data.values());
                result = entity;
            } catch (DataStoreException e) {
                log.error("Failed save object", e);
            }
        }

        return result;
    }

    @Override
    public EntityType update(EntityType entity) {
        EntityType result = null;
        Integer id = entity.getId();
        if (id == 0) {
            throw new IllegalArgumentException("Object is new");
        }
        EntityType oldEntity = data.get(entity.getId());
        indexes.forEach((key, items) -> {
            Field field = getFieldByName(dataStore.getObjClass(), key);
            field.setAccessible(true);
            try {
                if(oldEntity != null){
                    Object oldValue = field.get(oldEntity);
                    removeFromIndex(key, String.valueOf(oldValue), oldEntity.getId());
                }
                Object value = field.get(entity);
                addToIndex(key, String.valueOf(value), entity.getId());
            } catch (IllegalAccessException e) {
                log.error("Object didn't add to index", e);
            }
        });
        data.put(entity.getId(), entity);
        try {
            dataStore.save(data.values());
            result = entity;
        } catch (DataStoreException e) {
            log.error("Failed save object", e);
        }

        return result;
    }

    @Override
    public EntityType delete(Integer id) {
        EntityType result = null;
        if (!data.containsKey(id)) {
            throw new IllegalStateException("Object not found");
        }
        EntityType entity = data.remove(id);
        indexes.forEach((key, items) -> {
            Field field = getFieldByName(dataStore.getObjClass(), key);
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                removeFromIndex(key, String.valueOf(value), entity.getId());
            } catch (IllegalAccessException e) {
                log.error("Object didn't add to index", e);
            }
        });
        try {
            dataStore.save(data.values());
            result = entity;
        } catch (DataStoreException e) {
            log.error("Failed save object", e);
        }
        return result;
    }


    @Override
    public void addIndex(String field) {
        if (!indexes.containsKey(field)) {
            try {
                indexes.put(field, getIndex(field));
            } catch (IllegalAccessException e) {
                log.error("Index didn't add!", e);
            }
        }
    }

    @Override
    public void addIndexes(List<String> fields) {
        final Map<String, Map<String, List<Integer>>> tmp = new HashMap();
        tmp.putAll(indexes);
        try {
            for (String field : fields) {
                if (!indexes.containsKey(field)) {
                    tmp.put(field, getIndex(field));
                }
            }
            indexes = tmp;
        } catch (IllegalAccessException e) {
            log.error("Index didn't add!", e);
        }
    }

    private Map<String, List<Integer>> getIndex(String field) throws IllegalAccessException {
        Map result;
        if (!indexes.containsKey(field)) {
            result = new HashMap<String, List<Integer>>();
            Field declaredField = null;
            for (BaseEntity item : data.values()) {
                if(declaredField == null){
                    declaredField = getFieldByName(item.getClass(), field);
                }
                declaredField.setAccessible(true);
                Object value = declaredField.get(item);
                String uuid = UUID.nameUUIDFromBytes(String.valueOf(value).getBytes()).toString();
                List items = (List) result.getOrDefault(uuid, new ArrayList<>());
                items.add(item.getId());

                result.put(uuid, items);
            }
        } else {
            result = indexes.get(field);
        }

        return result;
    }

    private void addToIndex(String field, String value, Integer id){
        if (indexes.containsKey(field)) {
            String uuid = UUID.nameUUIDFromBytes(value.getBytes()).toString();
            List items = (List) indexes.get(field).getOrDefault(uuid, new ArrayList<>());
            items.add(id);
            indexes.get(field).put(uuid, items);
        }
    }

    private void removeFromIndex(String field, String value, Integer id){
        if (indexes.containsKey(field)) {
            String uuid = UUID.nameUUIDFromBytes(value.getBytes()).toString();
            List items = (List) indexes.get(field).getOrDefault(uuid, new ArrayList<>());
            if(items.size() < 2){
                indexes.get(field).remove(uuid);
            }else{
                items.remove(id);
                indexes.get(field).put(uuid, items);
            }
        }
    }

    private Field getFieldByName (Class clazz, String fieldName){
        Field result = null;

        for (Field tmpFiled: clazz.getDeclaredFields()) {
            Column column = tmpFiled.getAnnotation(Column.class);
            if(column != null && column.name().equals(fieldName)){
                result = tmpFiled;
            }
        }
        if(result == null){
            throw new IllegalArgumentException("Field doesn't exist in object");
        }

        return result;
    }

    private EntityType cloneItem(EntityType item){
        EntityType result = null;

        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream("item.out"));
            objectOutputStream.writeObject(item);
            objectOutputStream.close();

            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("item.out"));
            result = (EntityType) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Fail clone item", e);
        }

        return result;
    }
}
