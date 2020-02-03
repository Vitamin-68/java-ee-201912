package ua.ithillel.dnepr.common.test.repository.yevhen.kovalov;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.test.repository.TestEntity;
import ua.ithillel.dnepr.eugenekovalov.repository.crud.CrudRepoImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateManyNewEntities;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateOneNewEntity;

@Slf4j
class CsvCrudRepositoryIntegrationTest {
    private CrudRepository<TestEntity, Integer> crudRepository;

    @BeforeEach
    void setup() throws IOException {
        Path tempFile = Files.createTempFile("kovalov", ".csv");
        crudRepository = new CrudRepoImpl<>(tempFile, TestEntity.class);
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