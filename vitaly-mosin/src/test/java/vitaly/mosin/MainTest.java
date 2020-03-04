package vitaly.mosin;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static vitaly.mosin.Main.CSV_TYPE;
import static vitaly.mosin.Main.DEST_PATH;
import static vitaly.mosin.Main.DEST_TYPE;
import static vitaly.mosin.Main.FILE_PATH_DEST;
import static vitaly.mosin.Main.FILE_PATH_SOURCE;
import static vitaly.mosin.Main.H2_TYPE;
import static vitaly.mosin.Main.SOURCE_PATH;
import static vitaly.mosin.Main.SOURCE_TYPE;

class MainTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void main() {
        Main.main(new String[] {
                SOURCE_TYPE + "=" + CSV_TYPE,
                SOURCE_PATH + "=" + FILE_PATH_SOURCE,
                DEST_TYPE + "=" + H2_TYPE,
                DEST_PATH + "=" + FILE_PATH_DEST
        });
    }
}