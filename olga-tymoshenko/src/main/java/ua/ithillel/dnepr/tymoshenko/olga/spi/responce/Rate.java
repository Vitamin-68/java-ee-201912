package ua.ithillel.dnepr.tymoshenko.olga.spi.responce;
import lombok.Getter;
import lombok.ToString;
@Getter
@ToString
public class Rate {
    private String currency;
    private double saleRateNB;
    private double purchaseRateNB;
    private double saleRate;
    private double purchaseRate;

}