package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class BasePrefixFileRepository<EntityType extends AbstractEntity<IdType>, IdType> {
    protected static final String OBJECTS_SUB_PATH = "objects";
    protected final String repoRootPath;

    protected BasePrefixFileRepository(String repoRootPath) {
        this.repoRootPath = repoRootPath;
    }

    protected Path getPathEntity(String entityUuid) {
        return Path.of(
                repoRootPath,
                OBJECTS_SUB_PATH,
                entityUuid.substring(0, 2),
                entityUuid.substring(2, 4),
                entityUuid);
    }

    protected Set<Path> getPathEntities() {
        final Set<Path> result = new HashSet<>();
        final FileVisitor<Path> entityVisitor = new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                result.add(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                log.error("Failed to visit file: " + file.toString(), exc);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            if (Files.exists(Path.of(repoRootPath, OBJECTS_SUB_PATH))) {
                Files.walkFileTree(Path.of(repoRootPath, OBJECTS_SUB_PATH), entityVisitor);
            }
        } catch (IOException e) {
            log.error("Failed to walk entities", e);
        }
        return result;
    }
}
