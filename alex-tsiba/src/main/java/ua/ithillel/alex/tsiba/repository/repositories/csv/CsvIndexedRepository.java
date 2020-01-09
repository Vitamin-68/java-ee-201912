package ua.ithillel.alex.tsiba.repository.repositories.csv;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.alex.tsiba.repository.annotations.Column;
import ua.ithillel.alex.tsiba.repository.entity.AbstractEntity;
import ua.ithillel.alex.tsiba.repository.stores.DataStore;
import ua.ithillel.dnepr.common.repository.IndexedRepository;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class CsvIndexedRepository<EntityType extends AbstractEntity> extends AbstractRepository<EntityType>
        implements IndexedRepository {
    private Map<String, Map<String, List<Integer>>> indexes = new HashMap();

    public CsvIndexedRepository(DataStore dataStore) {
        super(dataStore);
    }

    @Override
    public void addIndex(String field) {
        if (!indexes.containsKey(field)) {
            try {
                indexes.put(field, getIndex(field));
            } catch (NoSuchFieldException | IllegalAccessException e) {
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Index didn't add!", e);
        }
    }

    private Map<String, List<Integer>> getIndex(String field) throws NoSuchFieldException, IllegalAccessException {
        Map result;
        if (!indexes.containsKey(field)) {
            result = new HashMap<String, List<Integer>>();
            Field declaredField = null;
            for (BaseEntity item : data.values()) {
                if(declaredField == null){
                    for (Field tmpFiled: item.getClass().getDeclaredFields()) {
                        Column column = tmpFiled.getAnnotation(Column.class);
                        if(column != null && column.name().equals(field)){
                            declaredField = tmpFiled;
                        }
                    }
                    if(declaredField == null){
                        throw new IllegalArgumentException("Field doesn't exist in object");

                    }
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
}
