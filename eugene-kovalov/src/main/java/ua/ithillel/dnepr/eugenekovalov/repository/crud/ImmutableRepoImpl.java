package ua.ithillel.dnepr.eugenekovalov.repository.crud;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class ImmutableRepoImpl<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseCrudRepo<EntityType> implements ImmutableRepository<EntityType, IdType> {

    public ImmutableRepoImpl(Path path, Class<EntityType> entityTypeClass) {
        super(path, entityTypeClass);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return getAllRecords();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return getAllRecords().stream()
                .flatMap(List::stream)
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        List<EntityType> listResult = new ArrayList<>();
        Object entityFieldValue = null;
        Field declaredField = null;

        for (EntityType entity : entitiesList(getAllRecords())) {
            try {
                declaredField = entity.getClass().getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                entityFieldValue = declaredField.get(entity);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error("Find by field error", e);
            }
            if (Objects.equals(value, entityFieldValue)) {
                listResult.add(entity);
            }
        }
        return listResult.isEmpty() ? Optional.empty() : Optional.of(listResult);
    }
}
