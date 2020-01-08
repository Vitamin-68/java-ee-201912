package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Country;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
public abstract class BaseFileRepository<EntityType> {
    protected final String repoRootPath;
    public static final String REGION_ID = "region_id";
    public static final String CITY_ID = "city_id";
    public static final String COUNTRY_ID = "country_id";
    public static final String NAME = "name";
    public static final char delimiter = ';';

    protected Class<EntityType> typeArgumentClass;
    protected  BaseFileRepository(String repoRootPath, Class<EntityType> typeArgumentClass){
        this.repoRootPath = repoRootPath;
        this.typeArgumentClass = typeArgumentClass;
    }

    protected Optional<EntityType> createEntity(CSVRecord csvRecord, Class<EntityType> typeArgumentClass){
        Optional<EntityType> result = Optional.empty();
        if(typeArgumentClass.isInstance(Country.class)){
            Country country = new Country();
            country.setId(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
            country.setCity_id(Integer.parseInt(csvRecord.get(CITY_ID)));
            country.setName(csvRecord.get(NAME));
            result = Optional.of((EntityType) country);
        }else if(typeArgumentClass.isInstance(Region.class)){
            Region region = new Region();
            region.setCountry_id(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
            region.setId(Integer.parseInt(csvRecord.get(REGION_ID)));
            region.setCity_id(Integer.parseInt(csvRecord.get(CITY_ID)));
            region.setName(csvRecord.get(NAME));
            result = Optional.of((EntityType) region);
        }else if(typeArgumentClass.isInstance(City.class)){
            City city = new City();
            city.setCountry_id(Integer.parseInt(csvRecord.get(COUNTRY_ID)));
            city.setRegion_id(Integer.parseInt(csvRecord.get(REGION_ID)));
            city.setId(Integer.parseInt(csvRecord.get(CITY_ID)));
            city.setName(csvRecord.get(NAME));
            result = Optional.of((EntityType) city);
        }
        return result;
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
}
