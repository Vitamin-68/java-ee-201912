package ua.ithillel.dnepr.tymoshenko.olga.ioc.validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ua.ithillel.dnepr.tymoshenko.olga.ioc.transfer.UtilTransfer;
import java.io.File;
import java.util.Map;

@Slf4j
public class ArgValidatorDecoratorDest extends ArgValidatorDecorator {
    public ArgValidatorDecoratorDest(ArgValidator validatorDecorator) {
        super(validatorDecorator);
    }

    @Override
    public void validate(Map<String, String> args) throws Exception {
        super.validate(args);
        UtilTransfer utilTransfer = new UtilTransfer();
        if (!args.containsKey("path-dest")) {
            throw new IllegalAccessException("There is not path dest");
        }
        String path = args.get("path-dest");
        if (StringUtils.isBlank(path)) {
            log.error("Path sourse is failed");
            throw new IllegalAccessException("Path source is failed");
        }

        String ext = utilTransfer.getFileExtension(new File(path));
        if (!ext.equals("csv") && !ext.isEmpty()) {
            log.error("Path destination is failed");
            throw new IllegalAccessException("Path destination is failed");
        }
    }
}
