package ua.ithillel.alex.tsiba.repository.repositories.csv;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.alex.tsiba.repository.entity.City;
import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.alex.tsiba.repository.stores.CSVDataStore;
import ua.ithillel.alex.tsiba.repository.stores.DataStore;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CsvMutableRepositoryTest {
    static CsvMutableRepository mutableRepository;
    static City city;

    @BeforeAll
    public static void setup() throws IOException, DataStoreException {
        city = new City();
        city.setCountryId(1);
        city.setName("test");
        city.setRegionId(1);

        DataStore dataStore = spy(new CSVDataStore(City.class));
        doNothing().when(dataStore).save(any());
        when(dataStore.load()).thenReturn(null);

        mutableRepository = new CsvMutableRepository(dataStore);
    }

    @Test
    void create() {
        assertEquals(city, mutableRepository.create(city));
    }

    @Test
    void update() {
        city.setName("TestUpdate");
        assertEquals(city, mutableRepository.update(city));
    }

    @Test
    void delete() {
        final City mockCity = mock(City.class);
        when(mockCity.getId()).thenReturn(2);
        mutableRepository.update(mockCity);
        assertEquals(mockCity, mutableRepository.delete(mockCity.getId()));
    }
}