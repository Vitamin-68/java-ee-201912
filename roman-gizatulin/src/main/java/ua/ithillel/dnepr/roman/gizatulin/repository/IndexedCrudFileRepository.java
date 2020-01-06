package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class IndexedCrudFileRepository<EntityType extends AbstractEntity<IdType>, IdType>
        extends BasePrefixFileRepository<EntityType, IdType>
        implements IndexedCrudRepository<EntityType, IdType> {
    private static final String INDEXES_SUB_PATH = "indexes";

    private final EntitySerializer<EntityType> entitySerializer;

    private final ImmutableRepository<EntityType, IdType> immutableRepository;
    private final MutableRepository<EntityType, IdType> mutableRepository;

    private final Set<IdType> ids = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> indexes = Collections.synchronizedSet(new HashSet<>());

    public IndexedCrudFileRepository(String repoRootPath, EntitySerializer<EntityType> entitySerializer) {
        super(repoRootPath);
        this.entitySerializer = entitySerializer;
        immutableRepository = new ImmutableRepositoryImp<>(repoRootPath, entitySerializer);
        mutableRepository = new MutableRepositoryImp<>(repoRootPath, entitySerializer);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return immutableRepository.findAll();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return immutableRepository.findById(id);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        Optional<List<EntityType>> result = Optional.empty();
        if (indexes.contains(fieldName)) {
            final Path indexPath = getIndexPath(fieldName, getUuidFromObject(value));
            final Set<String> existingIndexes = new HashSet<>();
            try {
                existingIndexes.addAll(new HashSet<>(Files.readAllLines(indexPath)));
            } catch (IOException e) {
                log.error("Failed to read index: {}", fieldName);
            }
            if (!existingIndexes.isEmpty()) {
                final Set<String> indexesToRemove = new HashSet<>();
                final List<EntityType> entities = new ArrayList<>();
                existingIndexes.forEach(entityUuid -> {
                    Path pathEntity = getPathEntity(entityUuid);
                    if (Files.exists(pathEntity)) {
                        try {
                            entities.add(entitySerializer.deserialize(Files.readAllBytes(pathEntity)));
                        } catch (IOException e) {
                            log.error("Failed to read entity", e);
                        }
                    } else {
                        indexesToRemove.add(entityUuid);
                    }
                });
                existingIndexes.removeAll(indexesToRemove);
                try {
                    Files.writeString(indexPath, String.join("\n", existingIndexes));
                } catch (IOException e) {
                    log.error("Failed to write updated indexes indexes", e);
                }
                result = Optional.of(entities);
            }
        } else {
            result = immutableRepository.findByField(fieldName, value);
        }
        return result;
    }

    @Override
    public EntityType create(EntityType entity) {
        EntityType result = mutableRepository.create(entity);
        createOrUpdateIndexes(entity);
        return result;
    }

    @Override
    public EntityType update(EntityType entity) {
        EntityType result = mutableRepository.update(entity);
        createOrUpdateIndexes(entity);
        return result;
    }

    @Override
    public EntityType delete(IdType id) {
        return mutableRepository.delete(id);
        //TODO: Remove indexes
    }

    @Override
    public void addIndex(String field) {
        indexes.add(field);
    }

    @Override
    public void addIndexes(List<String> fields) {
        indexes.addAll(fields);
    }

    private void createOrUpdateIndexes(EntityType entity) {
        if (indexes.isEmpty()) {
            return;
        }
        final Class<?> entityClass = entity.getClass();
        final Set<String> fields = Arrays.stream(entityClass.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());
        indexes.forEach(index -> {
            if (fields.contains(index)) {
                throw new IllegalArgumentException("Entity doesn't have indexed field: " + index);
            }
        });
        for (String index : indexes) {
            try {
                final Field field = entityClass.getDeclaredField(index);
                field.setAccessible(true);
                final Object fieldValue = field.get(entity);
                final Path indexPath = getIndexPath(index, getUuidFromObject(fieldValue));
                final Set<String> existingIndexes = new HashSet<>(Files.readAllLines(indexPath));
                existingIndexes.add(entity.getUuid());
                Files.writeString(indexPath, String.join("\n", existingIndexes));
            } catch (NoSuchFieldException | IllegalAccessException | IOException e) {
                log.error("Failed to create|update indexes", e);
            }
        }
    }

    private String getUuidFromObject(Object object) {
        return UUID.nameUUIDFromBytes(String.valueOf(object).getBytes()).toString();
    }

    private Path getIndexPath(String indexName, String uuid) {
        final String firstPart = uuid.substring(0, 2);
        final String secondPart = uuid.substring(2, 4);
        return Path.of(
                repoRootPath,
                INDEXES_SUB_PATH,
                indexName,
                firstPart,
                secondPart,
                uuid);
    }
}