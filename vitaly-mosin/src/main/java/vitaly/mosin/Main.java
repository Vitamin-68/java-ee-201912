package vitaly.mosin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.common.repository.entity.AbstractEntity;
import vitaly.mosin.ioc.AppConfig;
import vitaly.mosin.repository.csv.CityCrudRepository;
import vitaly.mosin.repository.csv.CountryCrudRepository;
import vitaly.mosin.repository.csv.RegionCrudRepository;
import vitaly.mosin.repository.jdbc.JdbcIndexedCrudRepository;

import java.io.File;
import java.util.List;

@Slf4j
public class Main {

    public static Class clazz = null;
    private static final String FILE_PATH_RESOURCE = "./vitaly-mosin/src/main/resources/";
    private static final String FILE_PATH_TMP = "./vitaly-mosin/target/classes/dev/db/";
    private static final String DB_FILE = "mainRepoVM";
    private static JdbcIndexedCrudRepository dbdRepo;

    public static void main(String[] args) {
        log.info("=== My application started ===");

        deleteDbFile(FILE_PATH_TMP + DB_FILE + ".mv.db");

        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class);

        CountryCrudRepository cnRepo = (CountryCrudRepository) appContext.getBean("countryCSV");
        List<?> countries = cnRepo.findAll().get();
        clazz = countries.get(0).getClass();
        dbdRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
        addDataToDb(countries);

        CityCrudRepository ctRepo = appContext.getBean(CityCrudRepository.class);
        List<?> cities = ctRepo.findAll().get();
        clazz = cities.get(0).getClass();
        dbdRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
        addDataToDb(cities);

        RegionCrudRepository rgRepo = appContext.getBean(RegionCrudRepository.class);
        List<?> regions = rgRepo.findAll().get();
        clazz = regions.get(0).getClass();
        dbdRepo = appContext.getBean(JdbcIndexedCrudRepository.class);
        addDataToDb(regions);

        appContext.close();
    }

    private static void addDataToDb(List<?> list) {
        list.stream().forEach(record -> dbdRepo.create((AbstractEntity) record));
    }

    private static void deleteDbFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }
}
