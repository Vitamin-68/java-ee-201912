package repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import repository.entity.City;
import repository.entity.Country;
import repository.entity.Region;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ImmutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseFileRepository
        implements ImmutableRepository<EntityType, IdType> {

    private Class<EntityType> typeArgumentClass;

    public ImmutableRepositoryImp(String repoRootPath, Class<EntityType> typeArgumentClass) {
        super(repoRootPath);
        this.typeArgumentClass = typeArgumentClass;
       // EntityType myNewT = typeArgumentClass.getDeclaredConstructor().newInstance();
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        Optional<List<EntityType>> result = Optional.empty();
        final List<EntityType> entities = new ArrayList<>();
        CSVParser parser = getParser();

        EntityFactory<IdType> entityFactory = new EntityFactory<IdType>();
        BaseEntity<IdType> baseEntity = entityFactory.getEntity(1);
        entities.add((EntityType) baseEntity);

        if (parser !=null) {
            try {
                for (CSVRecord csvRecord : parser.getRecords()) {
                    createEntity(csvRecord).ifPresent(entities::add);
                }
            } catch (IOException e) {
                log.error("parser.getRecords exception",e);
            }
            result = Optional.of(entities);
        }
        return result;
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return Optional.empty();
    }

    private CSVParser getParser(){
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

    private Optional<EntityType> createEntity(CSVRecord csvRecord){
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
}
