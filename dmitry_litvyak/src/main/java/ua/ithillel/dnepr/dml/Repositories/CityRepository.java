package ua.ithillel.dnepr.dml.Repositories;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.dml.domain.City;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//"city_id","country_id","region_id","name"
@Slf4j
public class CityRepository implements CrudRepository<City,Integer> {

    private final String filePath;

    public CityRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<List<City>> findAll() {
        Optional<List<City>> result = Optional.empty();

        final List<City> regions = new ArrayList<>();
        try{
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine: csvSourse.getRecords()
            ) {

                City region = new City();

                region.setRegion_id(Integer.parseInt(csvLine.get("region_id")));
                region.setId(Integer.parseInt(csvLine.get("city_id")));
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
    public Optional<City> findById(Integer id) {
        Optional<City> result = Optional.empty();
        try{
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));
            for (CSVRecord csvLine: csvSourse.getRecords()
            ) {
                if(Integer.parseInt(csvLine.get("city_id")) == id) {
                    result = Optional.of(getCity(csvLine));
                }
            }

        }catch(Exception e){

            log.error(e.toString());
        }
        return result;
    }

    @Override
    public Optional<List<City>> findByField(String fieldName, Object value) {
        Optional<List<City>> result = Optional.empty();
        try{
            CSVParser csvSourse = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(';')
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(filePath))));

            Map<String,Integer> header = csvSourse.getHeaderMap();
            List<City> cityList = new ArrayList<>();
            if(header.get(fieldName) != null && City.class.getDeclaredField(fieldName) != null){


                for (CSVRecord csvLine: csvSourse.getRecords()
                ) {
                    if(csvLine.get(fieldName).equals(value.toString())) {
                        City resultCity = getCity(csvLine);

                        cityList.add(resultCity);
                    }
                }
                result = Optional.of(cityList);
            }
        }catch(Exception e){

            log.error(e.toString());
        }
        return result;
    }

    @Override
    public City create(City entity) {
        Optional<City> test = findById(entity.getId());
        if (!test.isEmpty()) return test.get();
        try {
            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath),CSVFormat.DEFAULT.withHeader("city_id","country_id","region_id","name").withDelimiter(';'));
            csvPrinter.printRecord(entity.getId(),entity.getCountry_id(),entity.getRegion_id(),entity.getName());
            csvPrinter.flush();
            csvPrinter.close();
        } catch (IOException e) {
            log.error(e.toString());
        }
        return entity;
    }

    @Override
    public City update(City entity) {
        Optional<List<City>> allRecords = findAll();
        for (City currentRegion: allRecords.get()
        ) {
            if(currentRegion.getId().equals(entity.getId())){
                currentRegion.setRegion_id(entity.getRegion_id());
                currentRegion.setCountry_id(entity.getCountry_id());
                currentRegion.setName(entity.getName());
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath),CSVFormat.DEFAULT.withHeader("city_id","country_id","region_id","name").withDelimiter(';'));
                    for (City _region: allRecords.get()
                    ) {
                        csvPrinter.printRecord(_region.getId(),_region.getCountry_id(),_region.getRegion_id(),_region.getName());
                    }
                    csvPrinter.flush();
                    csvPrinter.close();
                } catch (IOException e) {
                    log.error(e.toString());
                }
                return currentRegion;
            }
        }
        return new City();
    }

    @Override
    public City delete(Integer id) {
        Optional<List<City>> allRecords = findAll();
        for (City currentRegion: allRecords.get()
        ) {
            if(currentRegion.getId().equals(id)){
                allRecords.get().remove(currentRegion);
                try {
                    CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(filePath),CSVFormat.DEFAULT.withHeader("city_id","country_id","region_id","name").withDelimiter(';'));
                    for (City _region: allRecords.get()
                    ) {
                        csvPrinter.printRecord(_region.getId(),_region.getCountry_id(),_region.getRegion_id(),_region.getName());
                    }
                    csvPrinter.flush();
                    csvPrinter.close();
                } catch (IOException e) {
                    log.error(e.toString());
                }
                return new City();
            }
        }
        return new City();
    }

    private City getCity(CSVRecord csvLine) {
        City region = new City();

        region.setRegion_id(Integer.parseInt(csvLine.get("region_id")));
        region.setId(Integer.parseInt(csvLine.get("city_id")));
        region.setCountry_id(Integer.parseInt(csvLine.get("country_id")));
        region.setName(csvLine.get("name"));
        return region;
    }
}
