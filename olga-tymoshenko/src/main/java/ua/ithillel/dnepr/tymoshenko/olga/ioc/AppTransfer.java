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



}
