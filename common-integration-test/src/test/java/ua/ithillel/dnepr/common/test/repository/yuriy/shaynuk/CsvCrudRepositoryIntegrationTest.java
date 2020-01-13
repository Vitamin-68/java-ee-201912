package ua.ithillel.dnepr.common.test.repository.yuriy.shaynuk;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.test.repository.TestEntity;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.CrudRepositoryImp;

import java.io.File;
import java.io.IOException;

import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateManyNewEntities;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateOneNewEntity;

@Slf4j
class CsvCrudRepositoryIntegrationTest {
    private CrudRepository<TestEntity, Integer> crudRepository;

    @BeforeEach
    void setup() throws IOException {
        File repoFile = ua.ithillel.dnepr.yuriy.shaynuk.repository.Utils.createTempFile("cityyy.csv");
        crudRepository = new CrudRepositoryImp<>(repoFile.getPath(),TestEntity.class) {
        };
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