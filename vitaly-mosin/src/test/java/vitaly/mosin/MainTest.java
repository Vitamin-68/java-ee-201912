package vitaly.mosin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.AssertTrue;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static vitaly.mosin.Main.CSV_TYPE;
import static vitaly.mosin.Main.DEST_PATH;
import static vitaly.mosin.Main.DEST_TYPE;
import static vitaly.mosin.Main.H2_TYPE;
import static vitaly.mosin.Main.SOURCE_PATH;
import static vitaly.mosin.Main.SOURCE_TYPE;


class MainTest {
    private static final String FILE_PATH_SOURCE_TEST = "./src/main/resources/dev/db/";
    private static final String FILE_PATH_DEST_TEST = "./target/classes/dev/db/";

    private static final String DB_FILE = "mainRepoVM_test";
    private static final String FILE_CITY_TEST = "city_test.csv";
    private static final String FILE_REGION_TEST = "region_test.csv";
    private static final String FILE_COUNTRY_TEST = "country_test.csv";

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void csvToDbTest() {
        Main.main(new String[]{
                SOURCE_TYPE + "=" + CSV_TYPE,
                SOURCE_PATH + "=" + FILE_PATH_SOURCE_TEST,
                DEST_TYPE + "=" + H2_TYPE,
                DEST_PATH + "=" + FILE_PATH_DEST_TEST
        });
        File file = new File(FILE_PATH_DEST_TEST + DB_FILE + ".mv.db");
        assertTrue(file.exists());
    }

    @Test
    void dbToCsvTest() {
        Main.main(new String[]{
                SOURCE_TYPE + "=" + H2_TYPE,
                SOURCE_PATH + "=" + FILE_PATH_SOURCE_TEST,
                DEST_TYPE + "=" + CSV_TYPE,
                DEST_PATH + "=" + FILE_PATH_DEST_TEST
        });
        File file = new File(FILE_PATH_DEST_TEST + FILE_CITY_TEST);
        assertTrue(file.exists());
        file = new File(FILE_PATH_DEST_TEST + FILE_COUNTRY_TEST);
        assertTrue(file.exists());
        file = new File(FILE_PATH_DEST_TEST + FILE_REGION_TEST);
        assertTrue(file.exists());
    }
}