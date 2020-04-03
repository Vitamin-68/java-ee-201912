package ua.ithillel.dnepr.tymoshenko.olga.spi;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ua.ithillel.dnepr.tymoshenko.olga.spi.impl.CurrencyRateUSDImpl;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyServiceTest {

    private String date = "01.01.2020";
    private CurrencyService currencyServiceUSD = null;
    private CurrencyService currencyServiceEUR = null;
    private CurrencyService currencyServiceGBR = null;

    @BeforeAll
    void setUp(){
        List<CurrencyService> services = ServiceProvider.getServices();
        services.forEach(currencyService -> {
            Enum<Currency> currency = currencyService.getCurrency();
            switch (currency.name()) {
                case "USD":
                    currencyServiceUSD = new CurrencyRateUSDImpl();
                    break;
                case "GBR":
                    currencyServiceGBR = new CurrencyRateUSDImpl();
                    break;
                case "EUR":
                    currencyServiceEUR = new CurrencyRateUSDImpl();
                    break;
            }
        });
    }

    @Test
    void currencyRateUSD() {
        Map<String, Double> actual = currencyServiceUSD.currencyRate(date);
        assertFalse(actual.isEmpty());
    }

    @Test
    void currencyRateEUR() {
        Map<String, Double> actual = currencyServiceEUR.currencyRate(date);
        assertFalse(actual.isEmpty());
    }

    @Test
    void currencyRateGBR() {
        Map<String, Double> actual = currencyServiceGBR.currencyRate(date);
        assertFalse(actual.isEmpty());
    }

    @Test
    void findByEmptyDate() {
        String test = "";

        try {
            Map<String, Double> actual = currencyServiceUSD.currencyRate(test);
            Assert.fail("Expected IOException");
        } catch (IllegalStateException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByBlankDate() {
        String test = " ";
        try {
            Map<String, Double> actual = currencyServiceUSD.currencyRate(test);
            Assert.fail("Expected IOException");
        } catch (IllegalStateException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByNullDate() {
        String test = null;
        try {
            Map<String, Double> actual = currencyServiceUSD.currencyRate(test);
            Assert.fail("Expected IOException");
        } catch (IllegalStateException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }

    @Test
    void findByWrongDate() {
        String test = "021.20";
        try {
            Map<String, Double> actual = currencyServiceUSD.currencyRate(test);
            Assert.fail("Expected IOException");
        } catch (IllegalStateException thrown) {
            Assert.assertNotEquals("", thrown.getMessage());
        }
    }
}