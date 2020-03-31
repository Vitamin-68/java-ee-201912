package ua.ithillel.dnepr.tymoshenko.olga.spi;
import java.util.Map;

public interface CurrencyService {
    Map<String, Double> currencyRate(String date);
    Enum<Currency> getCurrency();
}
