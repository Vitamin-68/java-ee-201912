package ua.ithillel.dnepr.yuriy.shaynuk.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.utils.H2Server;
import ua.ithillel.dnepr.common.utils.NetUtils;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.Utils;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.entity.City;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc.CqrsRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jpa.JpaCrudRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.TYPE_CSV;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.TYPE_JDBC;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.inputPath;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.inputType;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.outputPath;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.outputType;

@Slf4j
@Component
@SuppressWarnings("rawtypes")
public class IocRepository {
    @Bean(name = "input")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public CrudRepository<?, Integer> getInputRepository(){
        return getRepository(inputType, inputPath);
    }

    @Bean(name = "output")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public CrudRepository<?, Integer> getOutputRepository(){
        return getRepository(outputType, outputPath);
    }

    private CrudRepository<?, Integer> getRepository(String repoType, String path){
        CrudRepository<?, Integer> result = null;
        switch (repoType){
            case TYPE_CSV:  result = new CrudRepositoryImp<>(path, Program.getClazz(TYPE_CSV)); break;
            case TYPE_JDBC:
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit");
                EntityManager entityManager = entityManagerFactory.createEntityManager();
                result = new JpaCrudRepository<>(entityManager, Program.getClazz(TYPE_JDBC));

//                H2Server h2Server = new H2Server(NetUtils.getFreePort());
//                try {
//                    h2Server.start();
//                    Class.forName("org.sqlite.JDBC");
//                    Connection connection = DriverManager.getConnection("jdbc:sqlite:".concat(path));
//                    File dataFile = Utils.createTempFile("cityyy.csv");
//                    if (dataFile != null) {
//                        CrudRepository<?, Integer> crudRepository = new CrudRepositoryImp<>(dataFile.getPath(), Program.getClazz(TYPE_CSV));
//                        result = new CqrsRepositoryImp<>(connection, Program.getClazz(TYPE_CSV), crudRepository);
//                    }
//                } catch (SQLException | ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
                break;
        }
        return result;
    }
}
