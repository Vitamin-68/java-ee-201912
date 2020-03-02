package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.Utils;

@Slf4j
public class IocTest {

    @Test
    void start(){
        String inputPath = Utils.createTempFile("city.csv").toString();
        String outputPath = System.getProperty("java.io.tmpdir")+"\\test";
        Program.main(new String[]{"csv", inputPath, "jdbc", outputPath, "city"});
        Assertions.assertTrue(true);
    }
}
