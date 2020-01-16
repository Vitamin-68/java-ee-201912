package ua.ithillel.dnepr.common.test.repository.roman.gizatulin;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.test.repository.TestEntity;
import ua.ithillel.dnepr.roman.gizatulin.repository.EntitySerializer;
import ua.ithillel.dnepr.roman.gizatulin.repository.EntitySerializerImp;
import ua.ithillel.dnepr.roman.gizatulin.repository.IndexedCrudFileRepository;

import java.io.IOException;
import java.nio.file.Files;

import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateManyNewEntities;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateOneNewEntity;

@Slf4j
class FileIndexedCrudRepositoryIntegrationTest {
    private IndexedCrudFileRepository<TestEntity, Integer> crudRepository;

    @BeforeEach
    void setup() throws IOException {
        String repoRootPath = Files.createTempDirectory("romanGizatulin").toString();
        EntitySerializer<TestEntity> entitySerializer = new EntitySerializerImp<>();
        crudRepository = new IndexedCrudFileRepository<>(repoRootPath, entitySerializer);
    }

    @Test
    void createOneNewEntity() {
        testCreateOneNewEntity(crudRepository);
    }

    @Test
    void createManyNewEntities() {
        testCreateManyNewEntities(crudRepository);
    }
}