package ua.ithillel.dnepr.tymoshenko.olga.ioc.validator;
import java.util.Map;
import java.util.Objects;

public class ArgValidator {
    public void validate(Map<String, String> args) throws Exception {
        Objects.requireNonNull(args);
    }
}
