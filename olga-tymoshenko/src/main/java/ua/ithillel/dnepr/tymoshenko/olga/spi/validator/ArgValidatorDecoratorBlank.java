package ua.ithillel.dnepr.tymoshenko.olga.spi.validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ArgValidatorDecoratorBlank extends ArgValidatorDecorator {
    public ArgValidatorDecoratorBlank(ArgValidator validatorDecorator) {super(validatorDecorator);}

    @Override
    public void validate(String date) throws Exception {
        super.validate(date);
        if (StringUtils.isBlank(date)) {
            log.error("Field is blank");
            throw new IllegalArgumentException("Field is blank");
        }
    }
}

