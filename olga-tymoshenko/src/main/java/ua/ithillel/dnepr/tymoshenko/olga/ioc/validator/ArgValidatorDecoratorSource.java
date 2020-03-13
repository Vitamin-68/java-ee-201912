package ua.ithillel.dnepr.tymoshenko.olga.ioc.validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer.UtilTransfer;
import java.io.File;
import java.util.Map;

@Slf4j
public class ArgValidatorDecoratorSource extends ArgValidatorDecorator {
    public ArgValidatorDecoratorSource(ArgValidator validatorDecorator) {
        super(validatorDecorator);
    }

    @Override
    public void validate(Map<String, String> args) throws Exception {
        super.validate(args);
        UtilTransfer utilTransfer = new UtilTransfer();
        if (!args.containsKey("path-source")) {
            log.error("There is not path source");
            throw new IllegalAccessException("There is not path source");
        }
        String path = args.get("path-source");
        if (StringUtils.isBlank(path)) {
            log.error("Path source is empty");
            throw new IllegalAccessException("Path source is empty");
        }
        String ext = utilTransfer.getFileExtension(new File(path));
        if (!ext.equals("csv") && !ext.isEmpty()) {
            log.error("Path source is failed");
            throw new IllegalAccessException("Path source is failed");
        }
    }
}
