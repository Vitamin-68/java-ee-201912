package ua.ithillel.dnepr.tymoshenko.olga.spi.validator;

abstract public class ArgValidatorDecorator extends ArgValidator {
    private final ArgValidator validatorDecorator;

    public ArgValidatorDecorator(ArgValidator validatorDecorator) {this.validatorDecorator = validatorDecorator;}

    @Override
    public void validate(String date) throws Exception {
        validatorDecorator.validate(date);
    }
}
