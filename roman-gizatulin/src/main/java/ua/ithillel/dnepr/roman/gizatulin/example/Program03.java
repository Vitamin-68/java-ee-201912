
package ua.ithillel.dnepr.roman.gizatulin.example;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.common.Calc;

@Slf4j
@Setter
@Getter
public class Program03 {
    private Calc calc;

    public double addAndSubtract(double a, double b) {
        log.info("Input params: a={}; b={}", a, b);
        return calc.subtract(calc.add(a, b), b);
    }
}
