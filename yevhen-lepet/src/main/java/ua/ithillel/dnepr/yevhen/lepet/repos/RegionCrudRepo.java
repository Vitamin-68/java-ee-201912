package ua.ithillel.dnepr.yevhen.lepet.repos;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.yevhen.lepet.entity.City;
import ua.ithillel.dnepr.yevhen.lepet.entity.Region;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class RegionCrudRepo implements CrudRepository<Region, Integer> {
    private final String filePath;
    private final char delimiter;
    private final String REGION_ID = "region_id";
    private final String COUNTRY_ID = "country_id";
    private final String CITY_ID = "city_id";
    private final String NAME = "name";

    public RegionCrudRepo(String filePath, char delimiter) {
        this.filePath = filePath;
        this.delimiter = delimiter;
    }

    @Override
    public Optional<List<Region>> findAll() {
        Optional<List<Region>> result = Optional.empty();
        final List<Region> regions = new ArrayList<>();
        try {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                convertRegion(csvRecord);
                Region region = new Region();
                regions.add(region);
            }
            result = Optional.of(regions);
        } catch (IOException e) {
            log.error("Failed to read file", e);
        }
        return result;
    }



    @Override
    public Optional<Region> findById(Integer id) {
        Optional<Region> result = Optional.empty();
        try {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvRecord : csvParser.getRecords()) {
                if (Integer.parseInt(csvRecord.get(REGION_ID)) == id) {
                    result = Optional.of(convertRegion(csvRecord));
                }
            }
        } catch (IOException e) {
            log.error("Exception CSV " + e);
        }
        return result;
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        Optional<List<Region>> result = Optional.empty();
        try {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new FileReader(filePath));
            Map<String, Integer> header = csvParser.getHeaderMap();
            List<Region> searchRegion = new ArrayList<>();
            if (header.get(fieldName) != null) {
                if (City.class.getDeclaredField(fieldName) != null) {
                    for (CSVRecord csvRecord : csvParser.getRecords()) {
                        if (Objects.equals(csvRecord.get(fieldName), value.toString())) {
                            searchRegion.add(convertRegion(csvRecord));
                        }
                    }
                    result = Optional.of(searchRegion);
                }
            }
        } catch (Exception e) {
            log.error("Exception CSV " + e);
        }
        return result;
    }

    @Override
    public Region create(Region entity) {
        if (findById(entity.getId()).isEmpty()) {
            final List<Region> regions = findAll().get();
            regions.add(entity);
            try {
                CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                        .withHeader(REGION_ID, COUNTRY_ID, CITY_ID, NAME)
                        .withDelimiter(delimiter));
                for (Region region : regions) {
                    csvPrinter.printRecord(region.getId(),
                            region.getCountry_id(),
                            region.getCity_id(),
                            region.getName());
                    csvPrinter.flush();
                    csvPrinter.close();
                }
            } catch (IOException e) {
                log.error("Exception CSV " + e);
            }
        }
        return entity;
    }

    @Override
    public Region update(Region entity) {
        Optional<List<Region>> regions = findAll();
        for (Region region : regions.get()) {
            if (region.getId().equals(entity.getId())) {
                region.setCountry_id(entity.getCountry_id());
                region.setCity_id(entity.getCity_id());
                region.setName(entity.getName());
                addCSVPrinter((List<Region>) region);
                return entity;
            }
        }
        return entity;
    }

    @Override
    public Region delete(Integer id) {
        Optional<List<Region>> regions = findAll();
        for (Region region : regions.get()) {
            if (region.getId().equals(id)) {
                regions.get().remove(region);
                addCSVPrinter((List<Region>) region);
                return region;
            }
        }
        return new Region();
    }

    private Region convertRegion(CSVRecord csvRecord) {
        Region region = new Region();
        region.setId(Integer.parseInt(csvRecord.get(REGION_ID)));
        region.setCountry_id(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
        region.setCity_id(Integer.parseInt(csvRecord.get(CITY_ID)));
        region.setName(csvRecord.get(NAME));
        return region;
    }

    private void addCSVPrinter(List<Region> regions) {
        try {
            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath), CSVFormat.DEFAULT
                    .withHeader(REGION_ID, COUNTRY_ID, CITY_ID, NAME)
                    .withDelimiter(delimiter));
            for (Region someRegion : regions) {
                csvPrinter.printRecord(someRegion.getId(),
                        someRegion.getCountry_id(),
                        someRegion.getCity_id(),
                        someRegion.getName());
                csvPrinter.flush();
                csvPrinter.close();
            }
        } catch (IOException e) {
            log.error("Exception CSV " + e);
        }
    }
}

