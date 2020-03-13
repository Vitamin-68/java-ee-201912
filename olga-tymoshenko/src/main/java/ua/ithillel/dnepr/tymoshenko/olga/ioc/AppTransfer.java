package ua.ithillel.dnepr.tymoshenko.olga.ioc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgDecoratorEmpty;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgValidator;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgValidatorDecorator;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgValidatorDecoratorDest;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgValidatorDecoratorSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Configuration
@ComponentScan({
        "ua.ithillel.dnepr.tymoshenko.olga.ioc"
})
public class AppTransfer {


    public List<String> getArgs(String[] args) {
        List<String> list = new ArrayList<>();
        Map<String, String> params = null;
        for (String arg : args) {
            if (params == null) {
                params = new HashMap<>();
            }
            String[] keyValue = arg.split("=");
            params.put(keyValue[0], keyValue[1]);
        }
        ArgValidatorDecorator argValidator =
                new ArgDecoratorEmpty(
                        new ArgValidatorDecoratorSource(
                                new ArgValidatorDecoratorDest(new ArgValidator())
                        )
                );
        try {
            argValidator.validate(params);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        list.add(params.get("path-source"));
        list.add(params.get("path-dest"));

        return list;
    }
}
