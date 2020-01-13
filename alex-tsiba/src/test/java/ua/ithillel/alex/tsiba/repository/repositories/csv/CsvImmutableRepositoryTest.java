package ua.ithillel.alex.tsiba.repository.repositories.csv;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.alex.tsiba.repository.entity.City;
import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.alex.tsiba.repository.stores.CSVDataStore;
import ua.ithillel.alex.tsiba.repository.stores.DataStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class CsvImmutableRepositoryTest {
    static CsvImmutableRepository immutableRepository;
    static City city;

    @BeforeAll
    public static void setup() throws IOException, DataStoreException {
        city = new City();
        city.setId(1);
        city.setCountryId(1);
        city.setName("test");
        city.setRegionId(1);

        DataStore dataStore = spy(new CSVDataStore(City.class));
        doNothing().when(dataStore).save(any());
        when(dataStore.load()).thenReturn(city).thenReturn(null);

        immutableRepository = new CsvImmutableRepository(dataStore);
    }

    @Test
    void findAll() {
        assertTrue(immutableRepository.findAll().isPresent());
    }

    @Test
    void findById() {
        assertEquals(Optional.of(city), immutableRepository.findById(city.getId()));
    }

    @Test
    void findByField() {
        Optional result = immutableRepository.findByField("name", city.getName());
        assertTrue(result.isPresent());
        List list = (ArrayList) result.get();
        assertTrue(list.contains(city));
    }
}