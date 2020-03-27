package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ArgDecoratorEmpty extends ArgValidatorDecorator {
    public ArgDecoratorEmpty(ArgValidator validatorDecorator) {
        super(validatorDecorator);
    }
    @Override
    public void validate(String field) throws Exception {
        super.validate(field);
        if (StringUtils.isEmpty(field)) {
            log.error("Field is empty");
            throw new IllegalArgumentException("Field is empty");
        }
    }
}
