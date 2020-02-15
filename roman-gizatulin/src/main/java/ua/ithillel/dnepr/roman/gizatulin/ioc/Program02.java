package ua.ithillel.dnepr.roman.gizatulin.ioc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Program02 {
    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(AppEngine.class)
                .getBean(AppEngine.class)
                .start(args);
    }
}
