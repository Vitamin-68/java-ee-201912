package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class MutableRepositoryImp<EntityType extends AbstractEntity<IdType>, IdType>
        extends BaseCrudRepository<EntityType>
        implements MutableRepository<EntityType, IdType> {

    public MutableRepositoryImp(String repoRootPath, Class<EntityType> typeArgumentClass) {
        super(repoRootPath, typeArgumentClass);
    }

    @Override
    public EntityType create(EntityType entity) {
        List<EntityType> entityList = new ArrayList<>();
        entityList.add(entity);
        saveListToFile(entityList, false);

        return entity;
    }

    @Override
    public EntityType update(EntityType entity) {
        EntityType result = null;
        final Optional<List<EntityType>> entitiesList = getAllRecords();
        for(EntityType tmpEntity : entitiesList.get()){
            if(tmpEntity.getId().equals(entity.getId())){
                int index = entitiesList.get().indexOf(tmpEntity);
                entitiesList.get().set(index,entity);
                result = tmpEntity;
                break;
            }
        }
        saveListToFile(entitiesList.get(), true);
        return result;
    }

    @Override
    public EntityType delete(IdType id) {
        EntityType result = null;
        final Optional<List<EntityType>> entitiesList = getAllRecords();
        if(entitiesList.isPresent()) {
            Iterator<EntityType> it = entitiesList.get().iterator();
            while(it.hasNext()){
                EntityType tmpEntity = it.next();
                if (tmpEntity.getId().equals(id)) {
                    result = tmpEntity;
                    it.remove();
                    break;
                }
            }
            saveListToFile(entitiesList.get(),true);
        }
        return result;
    }

    private void saveListToFile(List<EntityType> entityList, Boolean clearFile){
        CSVPrinter csvPrinter;
        try {
            CSVParser parser = Utils.getParser(repoRootPath);
            List<String> headerNames = parser.getHeaderNames();
            try {
                parser.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            csvPrinter = Utils.getPrinter(repoRootPath, clearFile);

            if(clearFile){
                csvPrinter.printRecord(headerNames);
            }

            for (EntityType entity:entityList) {
                csvPrinter.print(entity.getId().toString());
                for (int i=1; i<headerNames.size();i++) {
                    String name = headerNames.get(i);
                    Field field = entity.getClass().getDeclaredField(name);
                    field.setAccessible(true);
                    if(field.canAccess(entity)) {
                        csvPrinter.print(field.get(entity).toString());
                    }
                }
                csvPrinter.println();
            }
            csvPrinter.close(true);
        } catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            log.error("save listToFile error",e);
        }
    }
}
