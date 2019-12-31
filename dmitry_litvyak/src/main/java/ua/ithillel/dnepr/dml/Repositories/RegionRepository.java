package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.dml.domain.Region;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class RegionRepository implements CrudRepository<Region,Integer> {

    private final String filePath;

    public RegionRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<Region>> findAll() {
        Optional<List<Region>> result = Optional.empty();

        final List<Region> regions = new ArrayList<>();
        try{
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine: csvSourse.getRecords()
                 ) {

                Region region = new Region();

                region.setCity_id(Integer.parseInt(csvLine.get("city_id")));
                region.setId(Integer.parseInt(csvLine.get("region_id")));
                region.setCountry_id(Integer.parseInt(csvLine.get("country_id")));
                region.setName(csvLine.get("name"));
                regions.add(region);
            }
            result = Optional.of(regions);
        }catch(Exception e){

            log.error(e.toString());
        }

        return result;
    }

    @Override
    public Optional<Region> findById(Integer id) {
        Optional<Region> result = Optional.empty();
        try{
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine: csvSourse.getRecords()
            ) {
                if(Integer.parseInt(csvLine.get("region_id")) == id) {
                    result = getRegion(result, csvLine);
                }
            }

        }catch(Exception e){

            log.error(e.toString());
        }
        return result;
    }

    @Override
    public Optional<Region> findByField(String fieldName, Object value) {
        Optional<Region> result = Optional.empty();
        try{
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));

            Map<String,Integer> header = csvSourse.getHeaderMap();
            if(header.get(fieldName) != null && Region.class.getDeclaredField(fieldName) != null){


            for (CSVRecord csvLine: csvSourse.getRecords()
            ) {
                    if(csvLine.get(fieldName).equals(value.toString())) {
                        result = getRegion(result, csvLine);

                        break;
                    }
                }
            }
        }catch(Exception e){

            log.error(e.toString());
        }
        return result;
    }

    private Optional<Region> getRegion(Optional<Region> result, CSVRecord csvLine) {
        Region region = new Region();

        region.setCity_id(Integer.parseInt(csvLine.get("city_id")));
        region.setId(Integer.parseInt(csvLine.get("region_id")));
        region.setCountry_id(Integer.parseInt(csvLine.get("country_id")));
        region.setName(csvLine.get("name"));
        result = Optional.of(region);
        return result;

    }

    @Override
    public Region create(Region entity) {
        Optional<Region> test = findById(entity.getId());
        if (!test.isEmpty()) return test.get();
        try {
            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath),CSVFormat.DEFAULT.withHeader("region_id","country_id","city_id","name").withDelimiter(';'));
            csvPrinter.printRecord(entity.getId(),entity.getCountry_id(),entity.getCity_id(),entity.getName());
            csvPrinter.flush();
        } catch (IOException e) {
            log.error(e.toString());
        }
        return entity;
    }

    @Override
    public Region update(Region entity) {
        Optional<List<Region>> allRecords = findAll();
        for (Region currentRegion: allRecords.get()
        ) {
            if(currentRegion.getId().equals(entity.getId())){
                currentRegion.setCity_id(entity.getCity_id());
                currentRegion.setCountry_id(entity.getCountry_id());
                currentRegion.setName(entity.getName());
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath),CSVFormat.DEFAULT.withHeader("region_id","country_id","city_id","name").withDelimiter(';'));
                    for (Region _region: allRecords.get()
                         ) {
                        csvPrinter.printRecord(_region.getId(),_region.getCountry_id(),_region.getCity_id(),_region.getName());
                    }
                    csvPrinter.flush();
                } catch (IOException e) {
                    log.error(e.toString());
                }
                return currentRegion;
            }
        }
        return new Region();
    }

    @Override
    public Region delete(Integer id) {
        Optional<List<Region>> allRecords = findAll();
        for (Region currentRegion: allRecords.get()
             ) {
            if(currentRegion.getId().equals(id)){
                allRecords.get().remove(currentRegion);
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath),CSVFormat.DEFAULT.withHeader("region_id","country_id","city_id","name").withDelimiter(';'));
                    for (Region _region: allRecords.get()
                    ) {
                        csvPrinter.printRecord(_region.getId(),_region.getCountry_id(),_region.getCity_id(),_region.getName());
                    }
                    csvPrinter.flush();
                } catch (IOException e) {
                    log.error(e.toString());
                }
                return new Region();
            }
        }
        return new Region();
    }
}
