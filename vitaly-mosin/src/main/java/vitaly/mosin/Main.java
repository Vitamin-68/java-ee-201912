package vitaly.mosin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import vitaly.mosin.ioc.AppConfig;
import vitaly.mosin.repository.csv.CityCrudRepository;
import vitaly.mosin.repository.csv.CountryCrudRepository;
import vitaly.mosin.repository.csv.RegionCrudRepository;
import vitaly.mosin.repository.entity.City;
import vitaly.mosin.repository.entity.Country;
import vitaly.mosin.repository.entity.Region;
import vitaly.mosin.repository.jdbc.JdbcIndexedCrudRepository;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class Main {

    public static Class clazz = null;
    public static String filePathCsv;
    private static final String FILE_PATH_SOURCE = "./vitaly-mosin/src/main/resources/dev/db/";
    private static final String FILE_PATH_DEST = "./vitaly-mosin/target/classes/dev/db/";
    private static final String DB_FILE = "mainRepoVM";
    private static final String FILE_CITY_TEST = "city_test.csv";
    private static final String FILE_REGION_TEST = "region_test.csv";
    private static final String FILE_COUNTRY_TEST = "country_test.csv";
    private static final String CITY = "city";
    private static final String REGION = "region";
    private static final String COUNTRY = "country";
    public static final String SOURCE_TYPE = "source type";
    public static final String SOURCE_PATH = "source path";
    public static final String DEST_TYPE = "dest type";
    public static final String DEST_PATH = "dest path";
    public static final String CSV_TYPE = "csv";
    public static final String H2_TYPE = "h2";
    //args for copy records from CSV to DB
//    private static final String[] INPUT_PARAMS = new String[]{
//            SOURCE_TYPE + "=" + CSV_TYPE,
//            SOURCE_PATH + "=" + FILE_PATH_SOURCE,
//            DEST_TYPE + "=" + H2_TYPE,
//            DEST_PATH + "=" + FILE_PATH_DEST
//    };

    //args for copy records from DB to CSV files
    private static final String[] INPUT_PARAMS = new String[]{
            SOURCE_TYPE + "=" + H2_TYPE,
            SOURCE_PATH + "=" + FILE_PATH_SOURCE,
            DEST_TYPE + "=" + CSV_TYPE,
            DEST_PATH + "=" + FILE_PATH_DEST
    };
    private static JdbcIndexedCrudRepository dbRepo;
    private static AnnotationConfigApplicationContext appContext;

    public Main(String[] args) {
    }

    public static void main(String[] args) {
        log.info("=== My application started ===");

        Main mainPrg = new Main(args);
        mainPrg.deleteDbFile(FILE_PATH_DEST + DB_FILE + "_test.mv.db");
        mainPrg.deleteDbFile(FILE_PATH_DEST + FILE_CITY_TEST);
        mainPrg.deleteDbFile(FILE_PATH_DEST + FILE_REGION_TEST);
        mainPrg.deleteDbFile(FILE_PATH_DEST + FILE_COUNTRY_TEST);

        Map<String, String> mapParams = new HashMap<>();
        if (args.length == 0) {
            args = INPUT_PARAMS;
        }
        mainPrg.addParamToMap(args, mapParams);
        switch (mapParams.get(SOURCE_TYPE)) {
            case CSV_TYPE:
                filePathCsv = mapParams.get(SOURCE_PATH);
                appContext = new AnnotationConfigApplicationContext(AppConfig.class);
                mainPrg.csvToDb(COUNTRY);
                mainPrg.csvToDb(REGION);
                mainPrg.csvToDb(CITY);
                break;
            case H2_TYPE:
                filePathCsv = mapParams.get(DEST_PATH);
                appContext = new AnnotationConfigApplicationContext(AppConfig.class);
                mainPrg.dbToCsv(COUNTRY, filePathCsv);
                mainPrg.dbToCsv(REGION, filePathCsv);
                mainPrg.dbToCsv(CITY, filePathCsv);

        }
//        ctRepo.setFilePath(FILE_PATH_DEST + FILE_CITY_TEST);
//        clazz = cities.get(0).getClass();
//        dbRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
//        Optional<List<?>> result = dbRepo.findAll();
//        result.ifPresent(list -> list.forEach(record -> ctRepo.create((City) record)));

        appContext.close();
    }

    private Map addParamToMap(String[] param, Map<String, String> map) {
        for (String arg : param) {
            String[] keyValue = arg.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        return map;
    }

    private void csvToDb(String className) {
        List<?> result = listEntities(className);
        clazz = result.get(0).getClass();
        dbRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
        addDataToDb(result);
    }

    private void dbToCsv(String className, String path) {
        Optional<List<?>> resultList;
        switch (className) {
            case COUNTRY:
                CountryCrudRepository cnRepo = appContext.getBean(CountryCrudRepository.class);
                cnRepo.setFilePath(path + FILE_COUNTRY_TEST);
                clazz = Country.class;
                dbRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
                resultList = dbRepo.findAll();
                resultList.ifPresent(list -> list.forEach(record -> cnRepo.create((Country) record)));
                break;
            case REGION:
                RegionCrudRepository rgRepo = appContext.getBean(RegionCrudRepository.class);
                rgRepo.setFilePath(path + FILE_REGION_TEST);
                clazz = Region.class;
                dbRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
                resultList = dbRepo.findAll();
                resultList.ifPresent(list -> list.forEach(record -> rgRepo.create((Region) record)));
                break;
            case CITY:
                CityCrudRepository ctRepo = appContext.getBean(CityCrudRepository.class);
                ctRepo.setFilePath(path + FILE_CITY_TEST);
                clazz = City.class;
                dbRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
                resultList = dbRepo.findAll();
                resultList.ifPresent(list -> list.forEach(record -> ctRepo.create((City) record)));
                break;
        }
    }

    private List<?> listEntities(String className) {
        CrudRepository crudRepo = (CrudRepository) appContext.getBean(className.concat("CSV"));
            return (List<?>) crudRepo.findAll().get();
    }

    private void addDataToDb(List<?> list) {
        list.forEach(record -> dbRepo.create((AbstractEntity) record));
    }

    private void deleteDbFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
