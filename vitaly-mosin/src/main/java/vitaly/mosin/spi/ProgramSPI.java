package vitaly.mosin.spi;

import java.util.List;

public class ProgramSPI {
    public static final double USD_UA = 27.5;
    public static final double USD_RU = 76.389;
    public static final double USD_EUR = 0.8234;
    private static final double amount = 50;

    public static void main(String[] args) {
        List<ExchangeRates> serviceList = ServiceProvider.getServices();
        serviceList.forEach(exchangeRates -> exchangeRates.getExRates(amount));
    }
}
