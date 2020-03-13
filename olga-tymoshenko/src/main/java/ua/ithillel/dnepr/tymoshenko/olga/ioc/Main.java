package ua.ithillel.dnepr.tymoshenko.olga.ioc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer.UtilTransfer;
import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppTransfer.class);
        List<String> list = ctx.getBean(AppTransfer.class).getArgs(args);
        FactoryTransferData transferData =
                ctx.getBean(FactoryTransferData.class, new Object[]{list.get(0), list.get(1)});

        UtilTransfer utilTransfer = new UtilTransfer();
        if (utilTransfer.getFileExtension(new File(list.get(0))).equals("csv")) {
            transferData.getTransferToBase().dataSending();
        } else {
            transferData.getTransferToFile().dataSending();
        }

    }
}
