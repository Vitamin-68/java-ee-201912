package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
        List<String> entityValuesList = new ArrayList<>();
        entityValuesList.add(entity.getId().toString());
        addEntityValueToList(entity,entityValuesList, COUNTRY_ID);
        addEntityValueToList(entity,entityValuesList, REGION_ID);
        addEntityValueToList(entity,entityValuesList, CITY_ID);
        addEntityValueToList(entity,entityValuesList, NAME);

        try {
            CSVPrinter csvPrinter = getPrinter();
            String namesString = String.join("\";\"", entityValuesList);
            csvPrinter.printRecord("\""+namesString+"\"");
            csvPrinter.flush();
            csvPrinter.close();
        } catch (IOException e) {
            log.error("CSV printer:", e);
        }
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
        saveListToFile(entitiesList.get());
        return result;
    }

    @Override
    public EntityType delete(IdType id) {
        EntityType result = null;
        final Optional<List<EntityType>> entitiesList = getAllRecords();
        for(EntityType tmpEntity : entitiesList.get()){
            if(tmpEntity.getId().equals(id)){
                entitiesList.get().remove(tmpEntity);
                result = tmpEntity;
                break;
            }
        }
        saveListToFile(entitiesList.get());
        return result;
    }

    private void saveListToFile(List<EntityType> listEntity){
        try {
            BufferedReader brTest = new BufferedReader(new FileReader(repoRootPath));
            String headerLine = brTest .readLine();

            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(repoRootPath), CSVFormat.DEFAULT.withHeader(headerLine).withDelimiter(delimiter).withQuote(null));
            csvPrinter.flush();
            csvPrinter.close();
            for(EntityType tmpEntity : listEntity){
                create(tmpEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addEntityValueToList(EntityType entity,List<String> entityValues, String fieldId){
        try {
            Field field = entity.getClass().getDeclaredField(fieldId);
            field.setAccessible(true);
            if(field.canAccess(entity)) {
                entityValues.add(field.get(entity).toString());
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //log.error("addEntityValueToList error:", e);
        }
    }
}
