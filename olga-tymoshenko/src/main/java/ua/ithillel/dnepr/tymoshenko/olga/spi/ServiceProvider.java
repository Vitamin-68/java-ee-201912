package ua.ithillel.dnepr.tymoshenko.olga.spi;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

@UtilityClass
public class ServiceProvider {

    public List<CurrencyService> getServices() {
        ServiceLoader<CurrencyService> loader = ServiceLoader
                .load(CurrencyService.class);
        List<CurrencyService> result = new ArrayList<>();
        loader.forEach(result::add);

        return Collections.unmodifiableList(result);
    }




       /* public Iterator<CurrencyService> getServices() {
            {
                ServiceLoader<CurrencyService> loader = ServiceLoader
                        .load(CurrencyService.class);

                return loader.iterator();
            }
        }*/


}
