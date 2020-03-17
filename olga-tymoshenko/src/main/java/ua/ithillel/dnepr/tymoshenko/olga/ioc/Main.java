package ua.ithillel.dnepr.tymoshenko.olga.ioc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer.UtilTransfer;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgDecoratorEmpty;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgValidator;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgValidatorDecorator;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgValidatorDecoratorDest;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.validator.ArgValidatorDecoratorSource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        List<String> list = validArgs(args);
         ApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppTransfer.class);
        FactoryTransferData transferData =
                ctx.getBean(FactoryTransferData.class, new Object[]{list.get(0), list.get(1)});
        UtilTransfer utilTransfer = new UtilTransfer();
        if (utilTransfer.getFileExtension(new File(list.get(0))).equals("csv")) {
            transferData.getTransferToBase().dataSending();
        } else {
            transferData.getTransferToFile().dataSending();
        }

    }

  private static List<String> validArgs(String[] args) {
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
