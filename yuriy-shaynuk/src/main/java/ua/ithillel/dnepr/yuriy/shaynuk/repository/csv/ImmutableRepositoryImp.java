package ua.ithillel.dnepr.yuriy.shaynuk.repository.csv;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class ImmutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseCrudRepository<EntityType>
        implements ImmutableRepository<EntityType, IdType> {

    public ImmutableRepositoryImp(String repoRootPath, Class<EntityType> typeArgumentClass) {
        super(repoRootPath, typeArgumentClass);
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return getAllRecords();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        Optional<EntityType> result = Optional.empty();
        CSVParser parser = Utils.getParser(repoRootPath);
        try {
            for (CSVRecord csvLine : parser.getRecords()) {
                if (Integer.parseInt(csvLine.get(0)) == (Integer)id) {
                    result = createEntity(csvLine);
                }
            }
        } catch (IOException e) {
            log.error("parser.findById exception",e);
        }
        return result;
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        CSVParser parser = Utils.getParser(repoRootPath);
        Optional<List<EntityType>> result;
        Map<String, Integer> header = parser.getHeaderMap();
        List<EntityType> entities = new ArrayList<>();
        if (header.get(fieldName) != null) {
            try {
                for (CSVRecord csvRecord : parser.getRecords()) {
                    if (Objects.equals(csvRecord.get(fieldName), value.toString())) {
                        createEntity(csvRecord).ifPresent(entities::add);
                    }
                }
            } catch (IOException e) {
                log.error("parser.findByField exception", e);
            }
        }
        return entities.isEmpty() ? Optional.empty() : Optional.of(entities);
    }
}
