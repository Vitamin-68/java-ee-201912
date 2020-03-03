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
import java.util.List;
import java.util.Optional;

@Slf4j
public class Main {

    public static Class clazz = null;
    public static String filePathCsv;
    private static final String FILE_PATH_RESOURCE = "./vitaly-mosin/src/main/resources/";
    private static final String FILE_PATH_TMP = "./vitaly-mosin/target/classes/dev/db/";
    private static final String DB_FILE = "mainRepoVM";
    private static final String FILE_CITY_TEST = "city_test.csv";
    private static final String FILE_REGION_TEST = "region_test.csv";
    private static final String FILE_COUNTRY_TEST = "country_test.csv";
    private static final String CITY = "city";
    private static final String REGION = "region";
    private static final String COUNTRY = "country";
    private static JdbcIndexedCrudRepository dbRepo;
    private static AnnotationConfigApplicationContext appContext;

    public Main(String[] args) {
    }

    public static void main(String[] args) {
        log.info("=== My application started ===");

        Main mainPrg = new Main(args);

        mainPrg.deleteDbFile(FILE_PATH_TMP + DB_FILE + ".mv.db");
        mainPrg.deleteDbFile(FILE_PATH_TMP + FILE_CITY_TEST);
        mainPrg.deleteDbFile(FILE_PATH_TMP + FILE_REGION_TEST);
        mainPrg.deleteDbFile(FILE_PATH_TMP + FILE_COUNTRY_TEST);

        filePathCsv = FILE_PATH_RESOURCE;

        appContext = new AnnotationConfigApplicationContext(AppConfig.class);
        mainPrg.scvToDb(COUNTRY);
        mainPrg.scvToDb(REGION);
        mainPrg.scvToDb(CITY);

        mainPrg.dbToCsv(COUNTRY, FILE_PATH_TMP);
        mainPrg.dbToCsv(REGION, FILE_PATH_TMP);
        mainPrg.dbToCsv(CITY, FILE_PATH_TMP);

//        ctRepo.setFilePath(FILE_PATH_TMP + FILE_CITY_TEST);
//        clazz = cities.get(0).getClass();
//        dbRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
//        Optional<List<?>> result = dbRepo.findAll();
//        result.ifPresent(list -> list.forEach(record -> ctRepo.create((City) record)));

        appContext.close();
    }

    private void scvToDb(String className) {
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
                resultList.ifPresent(list -> list.forEach(record -> cnRepo.create((Country)record)));
                break;
            case REGION:
                RegionCrudRepository rgRepo = appContext.getBean(RegionCrudRepository.class);
                rgRepo.setFilePath(path + FILE_REGION_TEST);
                clazz = Region.class;
                dbRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
                resultList = dbRepo.findAll();
                resultList.ifPresent(list -> list.forEach(record -> rgRepo.create((Region)record)));
                break;
            case CITY:
                CityCrudRepository ctRepo = appContext.getBean(CityCrudRepository.class);
                ctRepo.setFilePath(path + FILE_CITY_TEST);
                clazz = City.class;
                dbRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
                resultList = dbRepo.findAll();
                resultList.ifPresent(list -> list.forEach(record -> ctRepo.create((City)record)));                break;
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
