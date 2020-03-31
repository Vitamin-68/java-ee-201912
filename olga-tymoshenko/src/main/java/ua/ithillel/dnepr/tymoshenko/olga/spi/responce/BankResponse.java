package ua.ithillel.dnepr.tymoshenko.olga.spi.responce;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ua.ithillel.dnepr.tymoshenko.olga.spi.validator.ArgDecoratorEmpty;
import ua.ithillel.dnepr.tymoshenko.olga.spi.validator.ArgValidator;
import ua.ithillel.dnepr.tymoshenko.olga.spi.validator.ArgValidatorDecorator;
import ua.ithillel.dnepr.tymoshenko.olga.spi.validator.ArgValidatorDecoratorBlank;
import ua.ithillel.dnepr.tymoshenko.olga.spi.validator.ArgValidatorDecoratorDate;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BankResponse {
    private final String URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";

    public BankRate getBankRate(String date) {
        validateDate(date);

        OkHttpClient client;
        try {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            log.error("OkHttpClient  is failed " + e.getMessage());
            throw new IllegalStateException("OkHttpClient  is failed " + e.getMessage());
        }

        StringBuilder sb = new StringBuilder();
        String requestURL = sb.append(URL).append(date).toString();
        Request request = new Request.Builder()
                .url(requestURL)
                .build();
        String json;
        try {
            Response response = client.newCall(request).execute();
            json = response.body().string();
        } catch (IOException |NullPointerException e) {
            log.error( e.getMessage());
            throw new IllegalStateException( e.getMessage());
        }
        Gson gson = new Gson();
        return gson.fromJson(json, BankRate.class);
    }

    private void validateDate(String date){
        ArgValidatorDecorator argValidator =
                new ArgDecoratorEmpty(
                        new ArgValidatorDecoratorBlank(
                                new ArgValidatorDecoratorDate(new ArgValidator())
                        )
                );
        try {
            argValidator.validate(date);
        } catch (Exception e) {
            log.error("Field is not validate" + e);
            throw new IllegalStateException("Field is not validate" + e.getMessage());
        }
    }
}
