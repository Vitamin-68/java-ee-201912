package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator;
import java.util.Objects;

public class ArgValidator {
    public void validate(String field) throws Exception {
        Objects.requireNonNull(field);
    }
}
