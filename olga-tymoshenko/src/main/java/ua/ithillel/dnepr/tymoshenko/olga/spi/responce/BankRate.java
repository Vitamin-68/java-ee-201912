package ua.ithillel.dnepr.tymoshenko.olga.spi.responce;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;
import java.util.ArrayList;

@Getter
@ToString
public class BankRate {
    private String date;
    @SerializedName("exchangeRate")
    private ArrayList<Rate> list = new ArrayList<>();
}
