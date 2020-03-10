package ua.ithillel.dnepr.yuriy.shaynuk.spi;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

@UtilityClass
public class ServiceProvider {

    public synchronized static List<HelloService> getServices(){
        List<HelloService> result = new ArrayList<>();
        ServiceLoader<HelloService> serviceLoader = ServiceLoader.load(HelloService.class);
        serviceLoader.forEach(result::add);

        return Collections.unmodifiableList(result);
    }
}
