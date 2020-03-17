package ua.ithillel.dnepr.tymoshenko.olga.ioc.validator;
import java.util.Map;

public class ArgDecoratorEmpty extends ArgValidatorDecorator {
    public ArgDecoratorEmpty(ArgValidator validatorDecorator) {
        super(validatorDecorator);
    }

    @Override
    public void validate(Map<String, String> args) throws Exception {
        super.validate(args);
        if (args.isEmpty()) {
            throw new IllegalArgumentException("There is empty arguments");
        }

    }
}
