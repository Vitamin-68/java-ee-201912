package ua.ithillel.dnepr.dml.spi;

import java.util.List;

public class ProgramSpi {
    public static void main(String[] args) {
        List<Quote> services = ServiceProvider.getServices();
        services.forEach(quoteService -> quoteService.sayWisdom());
    }
}
