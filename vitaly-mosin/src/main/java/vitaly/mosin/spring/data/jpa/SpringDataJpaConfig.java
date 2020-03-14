package vitaly.mosin.spring.data.jpa;

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
        basePackages = {"vitaly.mosin.spring.data.jpa.repository"}
)
@EnableTransactionManagement
@ComponentScan(
        basePackages = {
                "vitaly.mosin.spring.data.jpa",
                "vitaly.mosin.spring.data.jpa.entity"
        }
)
public class SpringDataJpaConfig {
    @Bean("entityManagerFactory")
    @PersistenceContext(unitName = "persistence-spring-data-jpa")
    public EntityManagerFactory entityManager() {
        return Persistence.createEntityManagerFactory("persistence-spring-data-jpa");
    }

    @Bean(name = "transactionManager")
    public TransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManager());
        return jpaTransactionManager;
    }

}

