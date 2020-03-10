package ua.ithillel.dnepr.dml.spi;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

@UtilityClass
public class ServiceProvider {
    public synchronized static List<Quote> getServices() {
        List<Quote> result = new ArrayList<>();
        ServiceLoader<Quote> serviceLoader = ServiceLoader.load(Quote.class);
        serviceLoader.forEach(result::add);
        return Collections.unmodifiableList(result);
    }
}
