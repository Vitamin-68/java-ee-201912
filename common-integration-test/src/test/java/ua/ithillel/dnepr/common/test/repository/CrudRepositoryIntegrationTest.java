package ua.ithillel.dnepr.common.test.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.dml.Repositories.IndexedCrudRepositoryImpl;
import ua.ithillel.dnepr.roman.gizatulin.repository.EntitySerializer;
import ua.ithillel.dnepr.roman.gizatulin.repository.EntitySerializerImp;
import ua.ithillel.dnepr.roman.gizatulin.repository.IndexedCrudFileRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class CrudRepositoryIntegrationTest {
    @Test
    void romanGizatulinCreateOneNewEntity() throws IOException {
        String repoRootPath = Files.createTempDirectory("romanGizatulin").toString();
        EntitySerializer<TestEntity> entitySerializer = new EntitySerializerImp<>();
        IndexedCrudFileRepository<TestEntity, Integer> crudFileRepository = new IndexedCrudFileRepository<>(repoRootPath, entitySerializer);
        testCreateOneNewEntity(crudFileRepository);
    }

    @Test
    void romanGizatulinCreateManyNewEntities() throws IOException {
        String repoRootPath = Files.createTempDirectory("romanGizatulin").toString();
        EntitySerializer<TestEntity> entitySerializer = new EntitySerializerImp<>();
        IndexedCrudFileRepository<TestEntity, Integer> crudFileRepository = new IndexedCrudFileRepository<>(repoRootPath, entitySerializer);
        testCreateManyNewEntities(crudFileRepository);
    }

    @Test
    void dmitryLitvyakCreateManyNewEntities() throws IOException {
        String repoRootPath = Files.createTempDirectory("dmitryLitvyak").toString();
        testCreateManyNewEntities(new IndexedCrudRepositoryImpl<TestEntity, Integer>(repoRootPath));
    }

    @Test
    void dmitryLitvyakCreateOneNewEntity() throws IOException {
        String repoRootPath = Files.createTempDirectory("dmitryLitvyak").toString();
        testCreateOneNewEntity(new IndexedCrudRepositoryImpl<TestEntity, Integer>(repoRootPath));
    }

    private static void testCreateOneNewEntity(CrudRepository<TestEntity, Integer> crudRepository) {
        final String className = crudRepository.getClass().getName();
        log.info("Test repository: {}", className);

        assertFalse(crudRepository.findAll().isPresent(), className);

        TestEntity testEntity = createEntities(1).get(0);
        TestEntity createdEntity = crudRepository.create(testEntity);

        assertSame(testEntity, createdEntity, className);
        checkEntities(testEntity, createdEntity, className);

        assertEquals(1, crudRepository.findAll().get().size(), className);
        Optional<TestEntity> entityOptional = crudRepository.findById(testEntity.getId());
        assertTrue(entityOptional.isPresent());
        checkEntities(testEntity, entityOptional.get(), className);
    }

    private static void testCreateManyNewEntities(CrudRepository<TestEntity, Integer> crudRepository) {
        final String className = crudRepository.getClass().getName();
        log.info("Test repository: {}", className);

        final int testEntityCount = 50;

        assertFalse(crudRepository.findAll().isPresent(), className);

        final Map<Integer, TestEntity> testEntities = createEntities(testEntityCount)
                .stream()
                .collect(Collectors.toMap(TestEntity::getId, Function.identity()));
        testEntities.forEach((integer, testEntity) -> {
            TestEntity createdEntity = crudRepository.create(testEntity);
            assertSame(testEntity, createdEntity, className);
            checkEntities(testEntity, createdEntity, className);
        });
        Optional<List<TestEntity>> optionalEntityList = crudRepository.findAll();
        assertTrue(optionalEntityList.isPresent());
        assertEquals(testEntityCount, optionalEntityList.get().size(), className);
        optionalEntityList.get().forEach(testEntity -> {
            TestEntity newEntity = testEntities.get(testEntity.getId());
            checkEntities(testEntity, newEntity, className);
        });
        testEntities.forEach((entityId, testEntity) -> {
            Optional<TestEntity> optionalCreatedEntity = crudRepository.findById(entityId);
            assertTrue(optionalCreatedEntity.isPresent());
            assertNotSame(testEntity, optionalCreatedEntity.get(), className);
            checkEntities(testEntity, optionalCreatedEntity.get(), className);
        });
    }

    private static List<TestEntity> createEntities(int count) {
        List<TestEntity> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            TestEntity testEntity = new TestEntity();
            testEntity.setId(i);
            testEntity.setField1("filed_" + i);
            testEntity.setField2("filed_" + 1);
            testEntity.setField3(i + 1);
            result.add(testEntity);
        }
        return result;
    }

    private static void checkEntities(TestEntity first, TestEntity second, String errorMessage) {
        assertEquals(first.getId(), second.getId(), errorMessage);
        assertEquals(first.getUuid(), second.getUuid(), errorMessage);
        assertEquals(first.getFiled1(), second.getFiled1(), errorMessage);
        assertEquals(first.getFiled2(), second.getFiled2(), errorMessage);
        assertEquals(first.getField3(), second.getField3(), errorMessage);
    }
}