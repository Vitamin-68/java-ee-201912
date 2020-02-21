package ua.ithillel.dnepr.yuriy.shaynuk.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program;

@Slf4j
public class IocTest {
    private static Program testInstance;

    @BeforeAll
    static void setUp() {
        //testInstance = new Program();
    }

    @Test
    void start(){
        Program.main(new String[]{"csv", "G:\\IdeaProjects\\common\\target\\classes\\region.csv", "csv", "d:\\test\\test.csv", "city"});
        Assertions.assertTrue(true);
    }
}
