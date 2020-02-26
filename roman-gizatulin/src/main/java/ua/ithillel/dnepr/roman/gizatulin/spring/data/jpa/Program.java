package ua.ithillel.dnepr.roman.gizatulin.spring.data.jpa;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.ithillel.dnepr.common.utils.H2Server;

import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

public class Program {
    public static void main(String[] args) {
        final H2Server h2Server = new H2Server(12345);
        EntityManagerFactory entityManagerFactory = null;
        try {
            h2Server.start();

            final AnnotationConfigApplicationContext annotationConfigApplicationContext =
                    new AnnotationConfigApplicationContext(AppConfig.class);
            final AppEngine appEngine = annotationConfigApplicationContext.getBean(AppEngine.class);
            entityManagerFactory = annotationConfigApplicationContext.getBean(EntityManagerFactory.class);

            appEngine.start(args);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (entityManagerFactory != null) {
                entityManagerFactory.close();
            }
            h2Server.stop();
        }
    }
}
