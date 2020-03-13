package ua.ithillel.dnepr.tymoshenko.olga.ioc.validator;
import java.util.Map;

abstract public class ArgValidatorDecorator extends ArgValidator {
    private final ArgValidator validatorDecorator;
    public ArgValidatorDecorator(ArgValidator validatorDecorator) {
        this.validatorDecorator = validatorDecorator;
    }

    @Override
    public void validate(Map<String, String> args) throws Exception {
        validatorDecorator.validate(args);
    }
}
