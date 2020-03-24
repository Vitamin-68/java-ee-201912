package vitaly.mosin.spi.impl;

import lombok.extern.slf4j.Slf4j;
import vitaly.mosin.spi.ExchangeRates;

import static vitaly.mosin.spi.ProgramSPI.USD_EUR;
import static vitaly.mosin.spi.ServiceProvider.round;

@Slf4j
public class EuropeCurrency implements ExchangeRates {
    @Override
    public void getExRates(double amount) {
        log.info("$" + amount + " USD = "
                + round(amount * USD_EUR, 2) + " euro");
    }
}
