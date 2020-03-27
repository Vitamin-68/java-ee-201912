package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator;

abstract public class ArgValidatorDecorator extends ArgValidator {
    private final ArgValidator validatorDecorator;
    public ArgValidatorDecorator(ArgValidator validatorDecorator) {

        this.validatorDecorator = validatorDecorator;
    }

    @Override
    public void validate(String field) throws Exception {
        validatorDecorator.validate(field);
    }
}
