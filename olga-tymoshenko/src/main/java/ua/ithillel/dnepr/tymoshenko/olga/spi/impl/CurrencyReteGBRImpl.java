package ua.ithillel.dnepr.tymoshenko.olga.spi.impl;
import ua.ithillel.dnepr.tymoshenko.olga.spi.Currency;
import ua.ithillel.dnepr.tymoshenko.olga.spi.CurrencyService;
import ua.ithillel.dnepr.tymoshenko.olga.spi.responce.BankRate;
import ua.ithillel.dnepr.tymoshenko.olga.spi.responce.BankResponse;
import ua.ithillel.dnepr.tymoshenko.olga.spi.responce.Rate;
import java.util.HashMap;
import java.util.Map;

public class CurrencyReteGBRImpl implements CurrencyService {
    @Override
    public Map<String, Double> currencyRate(String date) {
        BankResponse bankResponse = new BankResponse();
        BankRate bankRate = bankResponse.getBankRate(date);
        Map<String, Double> rateCurrency = new HashMap<>();
        for (Rate rate : bankRate.getList()) {
            if ("GBR".equals(rate.getCurrency())) {
                rateCurrency.put("saleRateNB", rate.getSaleRateNB());
                rateCurrency.put("purchaseRateNB", rate.getPurchaseRateNB());
                rateCurrency.put("saleRate", rate.getSaleRate());
                rateCurrency.put("purchaseRate", rate.getPurchaseRate());
                break;
            }
        }
        return rateCurrency;
    }

    @Override
    public Enum<Currency> getCurrency() {
        return Currency.GBR;

    }
}
