package ua.ithillel.dnepr.common.test.repository.alex.tsiba;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.alex.tsiba.repository.repositories.csv.CsvIndexedCrudRepository;
import ua.ithillel.alex.tsiba.repository.stores.CSVDataStore;
import ua.ithillel.alex.tsiba.repository.stores.DataStore;
import ua.ithillel.dnepr.common.repository.IndexedCrudRepository;
import ua.ithillel.dnepr.common.test.repository.TestEntity;
import ua.ithillel.dnepr.dml.Repositories.IndexedCrudRepositoryImpl;

import java.io.IOException;
import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateManyNewEntities;
import static ua.ithillel.dnepr.common.test.repository.CrudRepositoryIntegrationTestHelper.testCreateOneNewEntity;

@Slf4j
class CsvCrudRepositoryIntegrationTest {
    private IndexedCrudRepository<TestEntity, Integer> crudRepository;

    @BeforeEach
    void setup() throws IOException {
        DataStore dataStore = mock(CSVDataStore.class);
        when(dataStore.fieldExist(any())).thenReturn(true);
        when(dataStore.getObjClass()).thenReturn(TestEntity.class);
        crudRepository = new CsvIndexedCrudRepository(dataStore);
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