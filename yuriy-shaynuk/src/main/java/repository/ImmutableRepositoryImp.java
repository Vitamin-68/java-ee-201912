package repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import repository.entity.CsvEntity;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ImmutableRepositoryImp<EntityType extends BaseEntity<IdType>, IdType>
        extends BaseFileRepository
        implements ImmutableRepository<EntityType, IdType> {

    public ImmutableRepositoryImp(String repoRootPath) {
        super(repoRootPath);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        Optional<List<CsvEntity>> result = Optional.empty();

        final List<CsvEntity> entities = new ArrayList<>();
        CSVParser parser = getParser();
        if (parser !=null) {
            try {
                for (CSVRecord csvLine : parser.getRecords()) {
                    CsvEntity csvEntity = new CsvEntity();
                    csvEntity.setRegion_id(Integer.parseInt(csvLine.get(REGION_ID)));
                    csvEntity.setId(Integer.parseInt(csvLine.get(CITY_ID)));
                    csvEntity.setCountry_id(Integer.parseInt(csvLine.get(COUNTRY_ID)));
                    csvEntity.setName(csvLine.get(NAME));
                    entities.add(csvEntity);
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
            log.error("getParcer exception",e);
        }
        return csvParser;
    }
}
