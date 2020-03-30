package ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

@Configuration
@EnableJpaRepositories(
        basePackages = {
                "ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.repository"
        }
)
@EnableTransactionManagement
@ComponentScan(
        basePackages = {
                "ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa",
                "ua.ithillel.dnepr.tymoshenko.olga.spring.data.jpa.entity"
        }
)
public class AppConfig {
    @Bean("entityManagerFactory")
    @PersistenceContext(unitName = "persistence-unit-spring-data-jpa")
    public EntityManagerFactory entityManager() {
        return Persistence.createEntityManagerFactory("persistence-unit-spring-data-jpa");
    }

    @Bean(name = "transactionManager")
    public TransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManager());
        return jpaTransactionManager;
    }
}
