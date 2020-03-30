package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.validator;
import lombok.extern.slf4j.Slf4j;
import ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.entity.City;
import java.lang.reflect.Field;

@Slf4j
public class ArgValidatorDecoratorCityField extends ArgValidatorDecorator {
    public ArgValidatorDecoratorCityField(ArgValidator validatorDecorator) {
        super(validatorDecorator);
    }
    @Override
    public void validate(String field) throws Exception {
        super.validate(field);

        boolean present = false;
        Class<City> clazz = City.class;
        Field[] listField = clazz.getDeclaredFields();
        for (Field f : listField) {
            if (f.getName().equals(field)) {
                present = true;
                break;
            }
        }
        if (!present) {
            log.error("There is not this field in thid entity");
            throw new IllegalArgumentException("There is not this field in this entity");
        }
    }


}

