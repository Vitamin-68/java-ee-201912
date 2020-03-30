package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ArgValidatorDecoratorBlank extends ArgValidatorDecorator {
    public ArgValidatorDecoratorBlank(ArgValidator validatorDecorator) {super(validatorDecorator);}
    @Override
    public void validate(String field) throws Exception {
        super.validate(field);
        if (StringUtils.isBlank(field)) {
            log.error("Field is blank");
            throw new IllegalArgumentException("Field is blank");
        }
    }
}

