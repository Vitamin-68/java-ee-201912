package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Country;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class BaseCrudRepository<EntityType> {
    protected final String repoRootPath;
    public static final String REGION_ID = "region_id";
    public static final String CITY_ID = "city_id";
    public static final String COUNTRY_ID = "country_id";
    public static final String NAME = "name";
    public static final char delimiter = ';';

    protected final Class<EntityType> typeArgumentClass;
    protected BaseCrudRepository(String repoRootPath, Class<EntityType> typeArgumentClass){
        this.repoRootPath = repoRootPath;
        this.typeArgumentClass = typeArgumentClass;
    }

    protected Optional<EntityType> createEntity(CSVRecord csvRecord){
        Optional<EntityType> result = Optional.empty();
        if(typeArgumentClass == Country.class){
            Country country = new Country();
            country.setId(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
            country.setCity_id(Integer.parseInt(csvRecord.get(CITY_ID)));
            country.setName(csvRecord.get(NAME));
            result = Optional.of((EntityType) country);
        }else if(typeArgumentClass == Region.class){
            Region region = new Region();
            region.setCountry_id(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
            region.setId(Integer.parseInt(csvRecord.get(REGION_ID)));
            region.setCity_id(Integer.parseInt(csvRecord.get(CITY_ID)));
            region.setName(csvRecord.get(NAME));
            result = Optional.of((EntityType) region);
        }else if(typeArgumentClass == City.class){
            City city = new City();
            city.setCountry_id(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
            city.setRegion_id(Integer.parseInt(csvRecord.get(REGION_ID)));
            city.setId(Integer.parseInt(csvRecord.get(CITY_ID)));
            city.setName(csvRecord.get(NAME));
            result = Optional.of((EntityType) city);
        }
        return result;
    }

    protected String getFieldId(){
        String fieldId="";
        if(typeArgumentClass == Country.class){
            fieldId = COUNTRY_ID;
        }else if(typeArgumentClass == Region.class){
            fieldId = REGION_ID;
        }else if(typeArgumentClass == City.class){
            fieldId = CITY_ID;
        }
        return fieldId;
    }

    protected CSVParser getParser(){
        CSVParser csvParser = null;
        try {
            csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withDelimiter(delimiter)
                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(repoRootPath))));
        } catch (IOException e) {
            log.error("getParser exception",e);
        }
        return csvParser;
    }

    protected Optional<List<EntityType>> getAllRecords() {
        Optional<List<EntityType>> result;
        final List<EntityType> entities = new ArrayList<>();
        CSVParser parser = getParser();

        try {
            for (CSVRecord csvRecord : parser.getRecords()) {
                createEntity(csvRecord).ifPresent(entities::add);
            }
        } catch (IOException e) {
            log.error("parser.getAllRecords exception", e);
        }
        result = Optional.of(entities);

        return result;
    }

    protected CSVPrinter getPrinter(){
        CSVPrinter csvPrinter = null;
        try {
            csvPrinter = new CSVPrinter(Files.newBufferedWriter(Path.of(repoRootPath), StandardOpenOption.APPEND), CSVFormat.DEFAULT.withDelimiter(delimiter).withQuote(null));
        } catch (IOException e) {
            log.error("getPrinter exception",e);
        }

//        CSVPrinter csvPrinter = null;
//        try {
//            if(typeArgumentClass == Country.class){
//                csvPrinter = new CSVPrinter(new FileWriter(repoRootPath), CSVFormat.DEFAULT.withHeader(COUNTRY_ID, CITY_ID, NAME).withDelimiter(delimiter).withQuote(null));
//            }else if(typeArgumentClass == Region.class){
//                csvPrinter = new CSVPrinter(new FileWriter(repoRootPath), CSVFormat.DEFAULT.withHeader(REGION_ID, COUNTRY_ID, CITY_ID, NAME).withDelimiter(delimiter).withQuote(null));
//            }else if(typeArgumentClass == City.class){
//                csvPrinter = new CSVPrinter(Files.newBufferedWriter(Path.of(repoRootPath), StandardOpenOption.APPEND), CSVFormat.DEFAULT.withHeader(CITY_ID, COUNTRY_ID, REGION_ID, NAME).withDelimiter(delimiter).withQuote(null));
//            }
//        } catch (IOException e) {
//            log.error("getPrinter exception",e);
//        }
        return csvPrinter;
    }
}
