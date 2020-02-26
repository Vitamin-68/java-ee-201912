package vitaly.mosin;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import vitaly.mosin.ioc.AppConfig;

@Slf4j
public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("=== My application started ===");
        new AnnotationConfigApplicationContext(AppConfig.class).getBean(AppConfig.class);

    }
}
