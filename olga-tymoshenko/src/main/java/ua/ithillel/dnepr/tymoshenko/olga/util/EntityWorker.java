package ua.ithillel.dnepr.tymoshenko.olga.util;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;
import ua.ithillel.dnepr.common.utils.H2TypeUtils;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class EntityWorker<EntityType extends AbstractEntity<IdType>, IdType extends Serializable> {
    private final EntityType entity;
    private final List<Field> listFields;

    public EntityWorker(EntityType entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        this.entity = entity;
        listFields = getFieldsEntity();
    }

    public List<Field> getFields() {
        return listFields;
    }

    public LinkedHashMap<String, String> getFieldAndType() {
        final LinkedHashMap<String, String> fieldsType = new LinkedHashMap<>();
        for (Field field : listFields) {
            fieldsType.put(field.getName(), H2TypeUtils.toH2Type(field.getType()));
        }
        return fieldsType;
    }

    public LinkedHashMap<String, Object> getFieldAndValue() {
        final LinkedHashMap<String, Object> fieldsValue = new LinkedHashMap<>();
        for (Field field : listFields) {
            try {
                fieldsValue.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                log.error("Failed to get field and value", e);
            }
        }
        return fieldsValue;
    }

    public List<Object> getValueField() {
        List<Object> list = new ArrayList<>();
        for (Field field : listFields) {
            try {
                list.add(field.get(entity));
            } catch (IllegalAccessException e) {
                log.error("Failed to get value field", e);
            }
        }
        return list;
    }

    private List<Field> getFieldsEntity() {
        List<Field> list = new ArrayList<>();
        Class<?> iterateType = entity.getClass();
        while (true) {
            for (Field field : iterateType.getDeclaredFields()) {
                list.add(field);
            }
            if (iterateType == BaseEntity.class || iterateType == AbstractEntity.class || iterateType == Object.class) {
                break;
            } else {
                iterateType = iterateType.getSuperclass();
            }
        }
        list.forEach(field -> field.setAccessible(true));
        return list;
    }

    public Field getFieldByName(String name) {
        Objects.requireNonNull(name, "Field name is undefined");
        Field field = null;
        for (Field f : listFields) {
            if (f.getName().equalsIgnoreCase(name)) {
                field = f;
                break;
            }
        }
        return field;
    }

    public EntityType setFields(LinkedHashMap<String, Object> fieldValue) {
        if (fieldValue.isEmpty()) {
            log.error("Failed to set fields. Field value map is empty");
            return null;
        }
        for (Field field : listFields) {
            for (Map.Entry entry : fieldValue.entrySet()) {
                if (field.getName().equalsIgnoreCase(entry.getKey().toString())) {
                    try {
                        field.set(entity, entry.getValue());
                    } catch (IllegalAccessException e) {
                        log.error("Failed to set fields", e);
                    }
                }
            }
        }
        return entity;
    }
}

