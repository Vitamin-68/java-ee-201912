package ua.ithillel.dnepr.tymoshenko.olga.spi.validator;
import lombok.extern.slf4j.Slf4j;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Slf4j
public class ArgValidatorDecoratorDate extends ArgValidatorDecorator {
    public ArgValidatorDecoratorDate(ArgValidator validatorDecorator) {

        super(validatorDecorator);
    }

    @Override
    public void validate(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyy");
        df.setLenient(false);
          df.parse(date);

    }
}

