package ua.ithillel.dnepr.dml.ioc;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;
import ua.ithillel.dnepr.dml.Repositories.CityRepository;
import ua.ithillel.dnepr.dml.domain.City;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * вариант запуска из командной строки для csv и H2
 * -i
 * "C:/work/hillel-homework/prj_hillel_java_ee_201912/dmitry_litvyak/target/classes/city.csv"
 * -o
 * jdbc:h2:file:file:/"C:/work/hillel-homework/prj_hillel_java_ee_201912/dmitry_litvyak/target/classes/dev/db/main"
 * -c
 * ua.ithillel.dnepr.dml.domain.City
 * -id
 * 4324
 */
class ProgrammTest {

    private String tmpPath;
    private String databaseNameOut, databaseNameIn;
    private City tmpCity;
    private String[] args;
    private final Integer OBJECT_ID = 100;

    @BeforeEach
    void setUp() throws IOException {
        tmpPath = Files.createTempDirectory(Path.of(System.getProperty("java.io.tmpdir")), "dml").toString();
        databaseNameOut = Files.createFile(Path.of(tmpPath + File.separator + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss")) + "out.csv")).toString();
        databaseNameIn = Files.createFile(Path.of(tmpPath + File.separator + LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss")) + "in.csv")).toString();
        args = new String[8];
        args[0] = "-i";
        args[1] = databaseNameOut;
        args[2] = "-o";
        args[3] = databaseNameIn;
        args[4] = "-id";
        args[5] = OBJECT_ID.toString();
        args[6] = "-c";
        args[7] = "ua.ithillel.dnepr.dml.domain.City";

        tmpCity = new City();
        tmpCity.setName("Hurgada");
        tmpCity.setId(OBJECT_ID);
        tmpCity.setCountry_id(10);
        tmpCity.setRegion_id(15);

        CityRepository cRepo = new CityRepository(databaseNameOut, ';');
        cRepo.create(tmpCity);
    }

    @Test
    public void transfer() {
        Programm.main(args);
        CityRepository cRepo = new CityRepository(databaseNameIn, ';');
        Optional<City> result = cRepo.findById(tmpCity.getId());
        assertTrue(result.isPresent());
    }

}