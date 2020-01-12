package ua.ithillel.dnepr.common.test.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.CrudRepository;

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
public class CrudRepositoryIntegrationTestHelper {
    public static void testCreateOneNewEntity(CrudRepository<TestEntity, Integer> crudRepository) {
        assertFalse(crudRepository.findAll().isPresent());

        TestEntity testEntity = createEntities(1).get(0);
        TestEntity createdEntity = crudRepository.create(testEntity);

        assertSame(testEntity, createdEntity);
        checkEntities(testEntity, createdEntity);

        assertEquals(1, crudRepository.findAll().get().size());
        Optional<TestEntity> entityOptional = crudRepository.findById(testEntity.getId());
        assertTrue(entityOptional.isPresent());
        checkEntities(testEntity, entityOptional.get());
    }

    public static void testCreateManyNewEntities(CrudRepository<TestEntity, Integer> crudRepository) {
        final int testEntityCount = 50;

        assertFalse(crudRepository.findAll().isPresent());

        final Map<Integer, TestEntity> testEntities = createEntities(testEntityCount)
                .stream()
                .collect(Collectors.toMap(TestEntity::getId, Function.identity()));
        testEntities.forEach((integer, testEntity) -> {
            TestEntity createdEntity = crudRepository.create(testEntity);
            assertSame(testEntity, createdEntity);
            checkEntities(testEntity, createdEntity);
        });
        Optional<List<TestEntity>> optionalEntityList = crudRepository.findAll();
        assertTrue(optionalEntityList.isPresent());
        assertEquals(testEntityCount, optionalEntityList.get().size());
        optionalEntityList.get().forEach(testEntity -> {
            TestEntity newEntity = testEntities.get(testEntity.getId());
            checkEntities(testEntity, newEntity);
        });
        testEntities.forEach((entityId, testEntity) -> {
            Optional<TestEntity> optionalCreatedEntity = crudRepository.findById(entityId);
            assertTrue(optionalCreatedEntity.isPresent());
            assertNotSame(testEntity, optionalCreatedEntity.get());
            checkEntities(testEntity, optionalCreatedEntity.get());
        });
    }

    public static List<TestEntity> createEntities(int count) {
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

    public static void checkEntities(TestEntity first, TestEntity second) {
        assertEquals(first.getId(), second.getId());
        assertEquals(first.getUuid(), second.getUuid());
        assertEquals(first.getField1(), second.getField1());
        assertEquals(first.getField2(), second.getField2());
        assertEquals(first.getField3(), second.getField3());
    }
}