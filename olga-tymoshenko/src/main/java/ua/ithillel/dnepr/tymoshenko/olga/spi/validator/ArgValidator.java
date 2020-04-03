package ua.ithillel.dnepr.tymoshenko.olga.spi.validator;
import java.util.Objects;

public class ArgValidator {
    public void validate(String date) throws Exception {Objects.requireNonNull(date);}
}
