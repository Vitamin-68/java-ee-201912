package ua.ithillel.dnepr.eugenekovalov.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.eugenekovalov.repository.entity.Region;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class RegionCrudRepo implements CrudRepository<Region, Integer> {

    private final char DIVIDER;
    private final Path PATH;
    private final String REGION_ID = "region_id";
    private final String COUNTRY_ID = "country_id";
    private final String CITY_ID = "city_id";
    private final String NAME = "name";

    public RegionCrudRepo(Path path, char divider) {
        PATH = path;
        DIVIDER = divider;
    }

    @Override
    public Optional<List<Region>> findAll() {
        Optional<List<Region>> result = Optional.empty();
        List<Region> regions = new ArrayList<>();

        try {
            Reader in = new FileReader(PATH.toFile());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withDelimiter(DIVIDER)
                    .withFirstRecordAsHeader()
                    .parse(in);

            for (CSVRecord record : records) {
                Region region = new Region();

                region.setId(Integer.parseInt(record.get(REGION_ID)));
                region.setCountryId(Integer.parseInt(record.get(COUNTRY_ID)));
                region.setCityId(Integer.parseInt(record.get(CITY_ID)));
                region.setName(record.get(NAME));

                regions.add(region);
            }

            result = Optional.of(regions);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    @Override
    public Optional<Region> findById(Integer id) {
        Optional<List<Region>> regions = findAll();
        return regions.stream()
                .flatMap(List::stream)
                .filter(region -> region.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        List<Region> listResult = new ArrayList<>();
        List<Region> regions = regionsList(findAll());

        Object entityFieldValue = null;
        Field declaredField = null;

        for (Region region : regions) {
            try {
                declaredField = region.getClass().getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                entityFieldValue = declaredField.get(region);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error(e.getMessage());
            }

            if (Objects.equals(value, entityFieldValue)) {
                listResult.add(region);
            }
        }

        return Optional.of(listResult);
    }

    @Override
    public Region create(Region entity) {
        writeCsv(List.of(entity));

        return entity;
    }

    @Override
    public Region update(Region entity) {
        if (findById(entity.getId()).isEmpty()) {
            log.error("Entity with ID: " + entity.getId() + " not found");
            return entity;
        }

        List<Region> regions = findAll()
                .stream()
                .flatMap(List::stream)
                .map(o -> o.getId().equals(entity.getId()) ? entity : o)
                .collect(Collectors.toList());

        writeCsv(regions);

        return entity;
    }

    @Override
    public Region delete(Integer id) {
        List<Region> regions = regionsList(findAll());
        Region region = findById(id).get();

        regions.removeIf(region1 -> region.getId().equals(region1.getId()));
        writeCsv(regions);

        return region;
    }

    private void writeCsv(List<Region> regions) {
        try (FileWriter writer = new FileWriter(PATH.toFile());
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader(REGION_ID, COUNTRY_ID, CITY_ID, NAME)
                     .withDelimiter(DIVIDER))) {
            for (Region region : regions) {
                csvPrinter.printRecord(
                        region.getId(),
                        region.getCountryId(),
                        region.getCityId(),
                        region.getName());
            }
            csvPrinter.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private List<Region> regionsList(Optional<List<Region>> regions) {
        return regions.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }
}
