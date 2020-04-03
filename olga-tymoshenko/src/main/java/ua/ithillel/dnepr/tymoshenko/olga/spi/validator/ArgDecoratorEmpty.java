package ua.ithillel.dnepr.tymoshenko.olga.spi.validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ArgDecoratorEmpty extends ArgValidatorDecorator {
    public ArgDecoratorEmpty(ArgValidator validatorDecorator) {
        super(validatorDecorator);
    }

    @Override
    public void validate(String date) throws Exception {
        super.validate(date);
        if (StringUtils.isEmpty(date)) {
            log.error("Field is empty");
            throw new IllegalArgumentException("Field is empty");
        }
    }
}
