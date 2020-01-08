package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ImmutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseFileRepository<EntityType>
        implements ImmutableRepository<EntityType, IdType> {

    public ImmutableRepositoryImp(String repoRootPath, Class<EntityType> typeArgumentClass) {
        super(repoRootPath, typeArgumentClass);
       // EntityType myNewT = typeArgumentClass.getDeclaredConstructor().newInstance();
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        Optional<List<EntityType>> result = Optional.empty();
        final List<EntityType> entities = new ArrayList<>();
        CSVParser parser = getParser();
        if (parser !=null) {
            try {
                for (CSVRecord csvRecord : parser.getRecords()) {
                    createEntity(csvRecord,typeArgumentClass).ifPresent(entities::add);
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

}
