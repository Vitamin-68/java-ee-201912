
package ua.ithillel.dnepr.roman.gizatulin.example;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class Program02 {
    public static int add(int a, int b) {
        log.info("Input params: a={}; b={}", a, b);

        return a + b;
    }
}
