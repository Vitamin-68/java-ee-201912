package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

@Slf4j
public class MutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BasePrefixFileRepository<EntityType, IdType>
        implements MutableRepository<EntityType, IdType> {
    private final EntitySerializer<EntityType> entitySerializer;

    public MutableRepositoryImp(String repoRootPath, EntitySerializer<EntityType> entitySerializer) {
        super(repoRootPath);
        this.entitySerializer = entitySerializer;
    }

    @Override
    public EntityType create(EntityType entity) {
        final Path entityPath = getPathEntity(entity.getUuid());
        if (Files.exists(entityPath)) {
            throw new IllegalArgumentException("Entity already exists");
        }
        return createOrUpdateEntity(entity);
    }

    @Override
    public EntityType update(EntityType entity) {
        final Path entityPath = getPathEntity(entity.getUuid());
        if (!Files.exists(entityPath)) {
            throw new IllegalArgumentException("Entity doesn't exist");
        }
        return createOrUpdateEntity(entity);
    }

    @Override
    public EntityType delete(IdType id) {
        EntityType result = null;
        final AbstractEntity<IdType> tmpAbstractEntity = new AbstractEntity<>() {
        };
        tmpAbstractEntity.setId(id);
        final Path entityPath = getPathEntity(tmpAbstractEntity.getUuid());
        if (Files.exists(entityPath)) {
            try {
                result = entitySerializer.deserialize(Files.readAllBytes(entityPath));
                Files.delete(entityPath);
            } catch (IOException e) {
                log.error("Failed to read|delete entity: {}", id);
                throw new IllegalArgumentException(e);
            }
        } else {
            log.warn("Entity doesn't exist");
        }
        return result;
    }

    private EntityType createOrUpdateEntity(EntityType entity) {
        Objects.requireNonNull(entity, "Entity is undefined");
        Objects.requireNonNull(entity.getId(), "Entity id is undefined");

        EntityType result;
        final Path entityPath = getPathEntity(entity.getUuid());
        try {
            if (!Files.exists(entityPath)) {
                Files.createDirectories(entityPath.getParent());
                Files.createFile(entityPath);
            }
            Files.write(entityPath, entitySerializer.serialize(entity), StandardOpenOption.TRUNCATE_EXISTING);
            result = entitySerializer.deserialize(Files.readAllBytes(entityPath));
        } catch (IOException e) {
            log.error("Failed to read|write entity", e);
            throw new IllegalArgumentException(e);
        }
        return result;
    }
}
