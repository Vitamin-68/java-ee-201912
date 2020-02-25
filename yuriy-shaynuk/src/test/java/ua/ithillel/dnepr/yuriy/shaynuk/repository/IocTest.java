package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.Utils;

import java.nio.file.Paths;

@Slf4j
public class IocTest {
    private static Program testInstance;

    @BeforeAll
    static void setUp() {
        //testInstance = new Program();
    }

    @Test
    void start(){
        String inputPath = Utils.createTempFile("city.csv").toString();
        String outputPath = Utils.createTempFile("test.db").toString();
        Program.main(new String[]{"csv", inputPath, "jdbc", outputPath, "city"});
        Assertions.assertTrue(true);
    }
}
