package ua.ithillel.dnepr.common.test.repository.dmitry.litvyak;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.test.repository.TestEntity;
import ua.ithillel.dnepr.dml.Repositories.IndexedCrudRepositoryImpl;

import java.io.IOException;
import java.nio.file.Files;

import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateManyNewEntities;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateOneNewEntity;

@Slf4j
class FileCrudRepositoryIntegrationTest {
    private IndexedCrudRepositoryImpl<TestEntity, Integer> crudRepository;

    @BeforeEach
    void setup() throws IOException {
        String repoRootPath = Files.createTempDirectory("dmitryLitvyak").toString();
        crudRepository = new IndexedCrudRepositoryImpl<>(repoRootPath);
    }

    @Test
    void createManyNewEntities() {
        testCreateManyNewEntities(crudRepository);
    }

    @Test
    void createOneNewEntity() {
        testCreateOneNewEntity(crudRepository);
    }
}