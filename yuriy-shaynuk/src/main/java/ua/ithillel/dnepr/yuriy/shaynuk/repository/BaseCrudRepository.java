package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Country;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.Region;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
//    public static final char delimiter = ';';

    protected final Class<EntityType> typeArgumentClass;
    protected BaseCrudRepository(String repoRootPath, Class<EntityType> typeArgumentClass){
        this.repoRootPath = repoRootPath;
        this.typeArgumentClass = typeArgumentClass;
    }

    protected Optional<EntityType> createEntity(CSVRecord csvRecord){
        Optional<EntityType> result = Optional.empty();
        try {
            EntityType entity = typeArgumentClass.getConstructor().newInstance();

            Field idField = typeArgumentClass.getSuperclass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, Integer.parseInt(csvRecord.get(0)));

            Field[] fields = typeArgumentClass.getDeclaredFields();
            for (Field field: fields) {
                String fieldName = field.getName();
                String value = csvRecord.get(fieldName);
                field.setAccessible(true);
                if(field.getType() == Integer.class || field.getType() == int.class){
                    field.set(entity, Integer.parseInt(value));
                }else {
                    field.set(entity, (field.getType()).cast(value));
                }
            }
            result = Optional.of(entity);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | NoSuchFieldException e) {
            log.error("create entity error",e);
        }

        return result;
    }

    protected Optional<List<EntityType>> getAllRecords() {
        Optional<List<EntityType>> result;
        final List<EntityType> entities = new ArrayList<>();
        CSVParser parser = Utils.getParser(repoRootPath);

        try {
            for (CSVRecord csvRecord : parser.getRecords()) {
                createEntity(csvRecord).ifPresent(entities::add);
            }
        } catch (IOException e) {
            log.error("parser.getAllRecords exception", e);
        }

        return entities.isEmpty() ? Optional.empty() : Optional.of(entities);
    }

//    protected CSVParser getParser(){
//        CSVParser csvParser = null;
//        try {
//            csvParser = CSVFormat.DEFAULT
//                    .withFirstRecordAsHeader()
//                    .withDelimiter(delimiter)
//                    .parse(new InputStreamReader(Files.newInputStream(Paths.get(repoRootPath))));
//        } catch (IOException e) {
//            log.error("getParser exception",e);
//        }
//        return csvParser;
//    }

//    protected CSVPrinter getPrinter(){
//        CSVPrinter csvPrinter = null;
//        try {
//            csvPrinter = new CSVPrinter(Files.newBufferedWriter(Path.of(repoRootPath), StandardOpenOption.APPEND), CSVFormat.DEFAULT.withDelimiter(delimiter).withQuote(null));
//        } catch (IOException e) {
//            log.error("getPrinter exception",e);
//        }
//        return csvPrinter;
//    }
}
