package ua.ithillel.alex.tsiba.repository;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CsvCrudRepositoryTest {
    static CsvCrudRepository crudRepository;
    static City city;

    @BeforeAll
    public static void setup() throws IOException, DataStoreException {
        city = new City();
        city.setCountryId(1);
        city.setName("test");
        city.setRegionId(1);

        DataStore dataStore = spy(new CSVDataStore(City.class));
        doNothing().when(dataStore).save(any(List.class));
        when(dataStore.load()).thenReturn(null);

        crudRepository = new CsvCrudRepository(dataStore);
    }

    @Test
    void findAll() {
        assertTrue(crudRepository.findAll().isPresent());
    }

    @Test
    void create() {
        assertEquals(city, crudRepository.create(city));
    }

    @Test
    void update() {
        city.setName("TestUpdate");
        assertEquals(city, crudRepository.update(city));
    }

    @Test
    void findById() {
        assertEquals(Optional.of(city), crudRepository.findById(city.getId()));
    }

    @Test
    void findByField() {
        Optional result = crudRepository.findByField("name", city.getName());
        assertTrue(result.isPresent());
        List list = (ArrayList) result.get();
        assertTrue(list.contains(city));
    }

    @Test
    void delete() {
        final City mockCity = mock(City.class);
        when(mockCity.getId()).thenReturn(2);
        crudRepository.update(mockCity);
        assertEquals(mockCity, crudRepository.delete(mockCity.getId()));
        assertEquals(Optional.empty(), crudRepository.findById(mockCity.getId()));
    }
}