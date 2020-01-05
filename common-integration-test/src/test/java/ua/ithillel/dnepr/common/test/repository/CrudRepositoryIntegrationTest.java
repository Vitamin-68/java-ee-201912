package ua.ithillel.dnepr.common.test.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class CrudRepositoryIntegrationTest {
    private final List<CrudRepository<TestEntity, Integer>> crudRepositories = new ArrayList<>();

    @BeforeEach
    void setupTest() throws Exception {
        crudRepositories.clear();
        crudRepositories.addAll(RomanGizatulinTestRepositoriesBuilder.buildRepositories());
    }

    @Test
    void shouldCreateOneNewEntity() {
        for (final CrudRepository<TestEntity, Integer> crudRepository : crudRepositories) {
            final String className = crudRepository.getClass().getName();
            log.info("Test repository: {}", className);

            assertTrue(crudRepository.findAll().isPresent(), className);
            assertTrue(crudRepository.findAll().get().isEmpty(), className);

            TestEntity testEntity = createEntities(1).get(0);
            TestEntity createdEntity = crudRepository.create(testEntity);

            assertNotSame(testEntity, createdEntity, className);
            checkEntities(testEntity, createdEntity, className);

            assertEquals(1, crudRepository.findAll().get().size(), className);
        }
    }

    @Test
    void shouldCreateManyNewEntities() {
        for (final CrudRepository<TestEntity, Integer> crudRepository : crudRepositories) {
            final String className = crudRepository.getClass().getName();
            log.info("Test repository: {}", className);

            final int testEntityCount = 50;

            assertTrue(crudRepository.findAll().isPresent(), className);
            assertTrue(crudRepository.findAll().get().isEmpty(), className);

            final Map<Integer, TestEntity> testEntities = createEntities(testEntityCount)
                    .stream()
                    .collect(Collectors.toMap(TestEntity::getId, Function.identity()));
            testEntities.forEach((integer, testEntity) -> {
                TestEntity createdEntity = crudRepository.create(testEntity);
                assertNotSame(testEntity, createdEntity, className);
                checkEntities(testEntity, createdEntity, className);
            });
            assertEquals(testEntityCount, crudRepository.findAll().get().size(), className);
        }
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