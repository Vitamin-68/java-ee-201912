package ua.ithillel.dnepr.tymoshenko.olga.ios;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.Main;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransferTest {
    final BaseTransferTest baseTransferTest = new BaseTransferTest();

    @Test
    void transferDataCvsToH2() throws SQLException, IOException {
        String sourse = "./target/classes/dev/db/data/region.csv";
        String dest = "./target/classes/dev/db/testios";
        baseTransferTest.clear(dest, sourse);

        StringBuilder sourcePath = new StringBuilder();
        sourcePath.append("path-source=").append(sourse);
        StringBuilder destPath = new StringBuilder();
        destPath.append("path-dest=").append(dest);
        Main.main(new String[]{sourcePath.toString(), destPath.toString()});
        assertTrue(baseTransferTest.isTableExist("REGION"));
    }

    @Test
    void transferDataH2ToCvs() throws SQLException, IOException {
        String sourse = "./target/classes/dev/db/testios";
        String dest = "./target/classes/dev/db/region.csv";
        baseTransferTest.clear(dest, sourse);
        baseTransferTest.createBase("./target/classes/dev/db/data/region.csv");

        StringBuilder sourcePath = new StringBuilder();
        sourcePath.append("path-source=").append(sourse);
        StringBuilder destPath = new StringBuilder();
        destPath.append("path-dest=").append(dest);
        Main.main(new String[]{sourcePath.toString(), destPath.toString()});
        assertTrue(Files.exists(new File(dest).toPath()));
    }
}
