package ua.ithillel.alex.tsiba.repository;

import ua.ithillel.alex.tsiba.repository.entity.City;
import ua.ithillel.alex.tsiba.repository.entity.Country;
import ua.ithillel.alex.tsiba.repository.entity.Region;
import ua.ithillel.alex.tsiba.repository.exception.DataStoreException;
import ua.ithillel.alex.tsiba.repository.repositories.csv.CsvIndexedRepository;
import ua.ithillel.alex.tsiba.repository.stores.CSVDataStore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, DataStoreException {
        CsvIndexedRepository repo = new CsvIndexedRepository<Region>(new CSVDataStore(Region.class));
        List fields = new ArrayList();
        fields.add("name");
        fields.add("country_id");
        repo.addIndexes(fields);
    }
}
