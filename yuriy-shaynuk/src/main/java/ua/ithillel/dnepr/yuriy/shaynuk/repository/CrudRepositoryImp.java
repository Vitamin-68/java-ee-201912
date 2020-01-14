package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVPrinter;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.ImmutableRepository;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Slf4j
public class CrudRepositoryImp<EntityType extends BaseEntity<IdType>, IdType> implements CrudRepository<EntityType, IdType> {
    private final ImmutableRepository<EntityType, IdType> immutableRepository;
    private final MutableRepository<EntityType, IdType> mutableRepository;

    public CrudRepositoryImp(String filePath,Class<EntityType> typeArgumentClass) {
        immutableRepository = new ImmutableRepositoryImp(filePath, typeArgumentClass);
        mutableRepository = new MutableRepositoryImp(filePath, typeArgumentClass);
        if(new File(filePath).length()==0){
           Field[] fields = typeArgumentClass.getDeclaredFields();
           addHeaderToFile(filePath,fields);
        }
    }

    @Override
    public Optional<List<EntityType>> findAll() {
        return immutableRepository.findAll();
    }

    @Override
    public Optional<EntityType> findById(IdType id) {
        return immutableRepository.findById(id);
    }

    @Override
    public Optional<List<EntityType>> findByField(String fieldName, Object value) {
        return immutableRepository.findByField(fieldName,value);
    }

    @Override
    public EntityType create(EntityType entity) {
        Optional<EntityType> existedEntity = findById(entity.getId());
        return existedEntity.orElseGet(() -> mutableRepository.create(entity));
    }

    @Override
    public EntityType update(EntityType entity) {
        return mutableRepository.update(entity);
    }

    @Override
    public EntityType delete(IdType id) {
        return mutableRepository.delete(id);
    }

    private void addHeaderToFile(String filePath,Field[] fields) {
        CSVPrinter csvPrinter = null;
        csvPrinter = Utils.getPrinter(filePath, false);
        try {

            csvPrinter.print("id");
            for (Field field : fields) {
                csvPrinter.print(field.getName());
            }
            csvPrinter.println();
            csvPrinter.close(true);
        } catch (IOException e) {
            log.error("header creation error", e);
        }
    }
}
