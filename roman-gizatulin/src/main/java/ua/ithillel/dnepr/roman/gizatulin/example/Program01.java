
package ua.ithillel.dnepr.roman.gizatulin.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Program01 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Program01.class);

    public int add(int a, int b) {
        LOGGER.info("Input params: a={}; b={}", a, b);
        return a + b;
    }
}
