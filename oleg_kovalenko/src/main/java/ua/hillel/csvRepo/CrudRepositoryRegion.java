package ua.hillel.csvRepo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.*;
import ua.hillel.entity.Region;
import ua.ithillel.dnepr.common.repository.CrudRepository;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@AllArgsConstructor
public class CrudRepositoryRegion implements CrudRepository<Region, Integer> {

    private String path;
    private char delimeter;

    @Override
    public Optional<List<Region>> findAll() {
        log.info("Count of records {}", readCsv().size());
        return Optional.of(readCsv());
    }

    @Override
    public Optional<Region> findById(Integer id) {
        Optional<Region> regions = readCsv()
                .stream()
                .filter(Region -> Region.getRegionId() == id)
                .findFirst();
        if (regions.isEmpty()) {
            log.info("id {} not found", id);
        }
        return regions;
    }

    @Override
    public Optional<List<Region>> findByField(String fieldName, Object value) {
        Class<Region> RegionReflect = Region.class;
        List<Region> regions = new ArrayList<>();

        try {
            Field fields = RegionReflect.getDeclaredField(fieldName);
            fields.setAccessible(true);
            Object changedValueType;

            if (fields.getType().equals("int")) {
                changedValueType = Integer.parseInt(value.toString());
            } else {
                changedValueType = value;
            }

            for (Region region : readCsv()) {
                if (fields.get(region).toString().equals(changedValueType)) {
                    regions.add(region);
                }
            }

            if (regions.isEmpty()) {
                log.info("value {} not found", value);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.info("Field not found {}", e.getMessage());
        }
        return Optional.of(regions);
    }

    @Override
    public Region create(Region entity) {
        List<Region> regions = readCsv();
        boolean flag = false;

        for (Region region : regions) {
            if (region.getRegionId() == entity.getRegionId()) {
                flag = true;
                break;
            }
        }
        if (flag) {
            log.error("Duplicate id  {}", entity.getRegionId());
        } else {
            regions.add(entity);
        }
        writeCsv(regions);
        log.info(entity + " created");
        return entity;
    }

    @Override
    public Region update(Region entity) {
        List<Region> regions = readCsv();
        for (int i = 0; i < regions.size(); i++) {
            if (regions.get(i).getRegionId() == entity.getRegionId()) {
                regions.set(i, entity);
            }
        }
        writeCsv(regions);
        log.info("{} updated", regions);
        return entity;
    }

    @Override
    public Region delete(Integer id) {
        List<Region> regions = readCsv();
        Region region = findById(id).get();
        regions.remove(region);

        if (region.getRegionId() == id) {
            writeCsv(regions);
            log.info("deleted {}", region);
        } else {
            log.info("can't find id {}", id);
        }
        return region;
    }

    private List<Region> readCsv() {
        List<Region> regions = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(path))) {
            CSVParser csvData = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL)
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimeter)
                    .parse(reader);

            for (CSVRecord csvRecord : csvData) {
                int RegionId = Integer.parseInt(csvRecord.get(0));
                int countryId = Integer.parseInt(csvRecord.get(1));
                int cityId = Integer.parseInt(csvRecord.get(2));
                String name = csvRecord.get(3);
                regions.add(new Region(RegionId, countryId, cityId, name));
            }
        } catch (IOException e) {
            log.error("File not found {}", e.getMessage());
        }
        return regions;
    }

    private void writeCsv(List<Region> regions) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(path));
             CSVPrinter printer = CSVFormat.DEFAULT
                     .withHeader("Region_id", "country_id", "region_id", "name")
                     .withQuoteMode(QuoteMode.ALL).withDelimiter(delimeter)
                     .print(writer)) {
            for (Region region : regions) {
                printer.printRecord(region.getRegionId(), region.getCountryId(), region.getCityId(), region.getName());
            }
        } catch (IOException e) {
            log.error("Can't write info {}", e.getMessage());
        }
    }
}
