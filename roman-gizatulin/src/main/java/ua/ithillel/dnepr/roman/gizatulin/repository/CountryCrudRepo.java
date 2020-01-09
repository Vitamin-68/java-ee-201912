package ua.ithillel.dnepr.roman.gizatulin.repository;

import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.roman.gizatulin.repository.entity.Country;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CountryCrudRepo implements CrudRepository<Country, Integer> {
    private final String filePath;

    public CountryCrudRepo(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<Country>> findAll() {
        Optional<List<Country>> result = Optional.empty();
        final List<Country> countries = new ArrayList<>();
        try {
            List<String> strings = Files.readAllLines(Paths.get(filePath));
            for (String line : strings) {
                String[] field = line.split(";");
                Country country = new Country();
                country.setId(Integer.parseInt(field[0]));
                country.setName(field[2]);
                countries.add(country);
            }
            result = Optional.of(countries);
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }

    @Override
    public Optional<Country> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Country>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    @Override
    public Country create(Country entity) {
        return null;
    }

    @Override
    public Country update(Country entity) {
        return null;
    }

    @Override
    public Country delete(Integer id) {
        return null;
    }
}
