package vitaly.mosin.spi;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

@UtilityClass
public class ServiceProvider {
    synchronized static List<ExchangeRates> getServices(){
        List<ExchangeRates> result = new ArrayList<>();
        ServiceLoader<ExchangeRates> serviceLoader = ServiceLoader.load(ExchangeRates.class);
        serviceLoader.forEach(result::add);

        return Collections.unmodifiableList(result);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
