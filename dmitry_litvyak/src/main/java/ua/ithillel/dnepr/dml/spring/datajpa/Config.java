package ua.ithillel.dnepr.dml.spring.datajpa;

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
@EnableJpaRepositories(basePackages = {"ua.ithillel.dnepr.dml.spring.datajpa"})
@EnableTransactionManagement
@ComponentScan(basePackages = {"ua.ithillel.dnepr.dml.domain.jpa"})

public class Config {

    /**
     * если нужно что бы этот бин назывался по другому то в аннотации выше (или в xml) нужно прописать так:
     * @EnableJpaRepositories(basePackages="your.package", entityManagerFactoryRef="%emf%")
     * */
    @Bean(name = "entityManagerFactory")
    @PersistenceContext(unitName = "spring-jpa-unit-dml")
    public EntityManagerFactory getEntityManager() {
        return Persistence.createEntityManagerFactory("spring-jpa-unit-dml");
    }

    @Bean(name = "DMLTransactionManager")
    public TransactionManager getTransactionManager(){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(getEntityManager());
        return jpaTransactionManager;
    }
}
