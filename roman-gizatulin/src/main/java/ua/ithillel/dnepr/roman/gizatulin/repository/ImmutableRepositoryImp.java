package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Slf4j
public class ImmutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BasePrefixFileRepository<EntityType, IdType>
        implements ImmutableRepository<EntityType, IdType> {
    private final EntitySerializer<EntityType> entitySerializer;

    public ImmutableRepositoryImp(String repoRootPath, EntitySerializer<EntityType> entitySerializer) {
        super(repoRootPath);
        this.entitySerializer = entitySerializer;
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        final List<EntityType> result = new ArrayList<>();
        final Set<Path> pathEntities = getPathEntities();
        for (Path path : pathEntities) {
            try {
                final byte[] bytes = Files.readAllBytes(path);
                result.add(entitySerializer.deserialize(bytes));
            } catch (IOException e) {
                log.error("Failed to read entity", e);
            }
        }
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        Optional<EntityType> result = Optional.empty();
        final AbstractEntity<IdType> tmpAbstractEntity = new AbstractEntity<>() {
        };
        tmpAbstractEntity.setId(id);
        final Path entityPath = getPathEntity(tmpAbstractEntity.getUuid());
        if (Files.exists(entityPath)) {
            try {
                result = Optional.of(entitySerializer.deserialize(Files.readAllBytes(entityPath)));
            } catch (IOException e) {
                log.error("Failed to read entity", e);
                throw new IllegalArgumentException(e);
            }
        }
        return result;
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        final List<EntityType> result = new ArrayList<>();
        final Set<Path> pathEntities = getPathEntities();
        for (Path path : pathEntities) {
            try {
                EntityType entity = entitySerializer.deserialize(Files.readAllBytes(path));
                Field declaredField = entity.getClass().getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                Object entityFieldValue = declaredField.get(entity);
                if (Objects.equals(value, entityFieldValue)) {
                    result.add(entity);
                }
            } catch (IOException e) {
                log.error("Failed read entity", e);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("Entity doesn't have field: {}", fieldName);
            }
        }
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }
}
