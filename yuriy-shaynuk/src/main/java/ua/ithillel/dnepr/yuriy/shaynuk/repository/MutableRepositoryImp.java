package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import ua.ithillel.dnepr.common.repository.MutableRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
        saveAllToFile(entityList, false);

//        Field[] fields = entity.getClass().getDeclaredFields();
//        List<String> entityValuesList = new ArrayList<>();
//        for (Field field : fields) {
//
//        }
//        field.setAccessible(true);
//        if(field.canAccess(entity)) {
//            entityValuesList.add(field.get(entity).toString());
//        }

//        try {
//            CSVPrinter csvPrinter = Utils.getPrinter(repoRootPath);
//            String namesString = String.join("\";\"", entityValuesList);
//            csvPrinter.printRecord("\""+namesString+"\"");
//            csvPrinter.flush();
//            csvPrinter.close();
//        } catch (IOException e) {
//            log.error("CSV printer:", e);
//        }

//        List<String> entityValuesList = new ArrayList<>();
//        entityValuesList.add(entity.getId().toString());
//        addEntityValueToList(entity,entityValuesList, COUNTRY_ID);
//        addEntityValueToList(entity,entityValuesList, REGION_ID);
//        addEntityValueToList(entity,entityValuesList, CITY_ID);
//        addEntityValueToList(entity,entityValuesList, NAME);
//
//        try {
//            CSVPrinter csvPrinter = getPrinter();
//            String namesString = String.join("\";\"", entityValuesList);
//            csvPrinter.printRecord("\""+namesString+"\"");
//            csvPrinter.flush();
//            csvPrinter.close();
//        } catch (IOException e) {
//            log.error("CSV printer:", e);
//        }
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
            }
        }
        saveAllToFile(entitiesList.get(), true);
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
            saveAllToFile(entitiesList.get(),true);
            //saveListToFile(entitiesList.get());
        }
        return result;
    }

    private void saveAllToFile(List<EntityType> entityList, Boolean clearFile){
        CSVPrinter csvPrinter;
        try {
            CSVParser parser = Utils.getParser(repoRootPath);
            List<String> headerNames = parser.getHeaderNames();
            try {
                parser.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(clearFile){
                csvPrinter = new CSVPrinter(Files.newBufferedWriter(Path.of(repoRootPath)),
                        CSVFormat.DEFAULT.withDelimiter(Utils.delimiter).withQuoteMode(QuoteMode.ALL).withFirstRecordAsHeader());
                csvPrinter.printRecord(headerNames);
            }else {
                csvPrinter = Utils.getPrinter(repoRootPath);
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
            e.printStackTrace();
        }
    }
//    private void saveListToFile(List<EntityType> listEntity){
//        try {
//            BufferedReader brTest = new BufferedReader(new FileReader(repoRootPath));
//            String headerLine = brTest .readLine();
//
//            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(repoRootPath), CSVFormat.DEFAULT.withHeader(headerLine).withDelimiter(Utils.delimiter).withQuote(null));
//            csvPrinter.flush();
//            csvPrinter.close();
//            for(EntityType tmpEntity : listEntity){
//                create(tmpEntity);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//    }
//    private void addEntityValueToList(EntityType entity,List<String> entityValues, String fieldId){
//        try {
//            Field field = entity.getClass().getDeclaredField(fieldId);
//            field.setAccessible(true);
//            if(field.canAccess(entity)) {
//                entityValues.add(field.get(entity).toString());
//            }
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            //log.error("addEntityValueToList error:", e);
//        }

//    }
}
