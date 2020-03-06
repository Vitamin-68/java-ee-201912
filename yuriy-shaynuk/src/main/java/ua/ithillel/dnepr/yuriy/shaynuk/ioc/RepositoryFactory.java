package ua.ithillel.dnepr.yuriy.shaynuk.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.csv.CrudRepositoryImp;
import ua.ithillel.dnepr.yuriy.shaynuk.repository.jdbc.JdbcCrudRepositoryImpl;

import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.TYPE_CSV;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.TYPE_JDBC;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.inputPath;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.inputType;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.outputPath;
import static ua.ithillel.dnepr.yuriy.shaynuk.ioc.Program.outputType;

@Slf4j
@Component
@SuppressWarnings("rawtypes")
public class RepositoryFactory {
    @Bean(name = "input")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public CrudRepository<BaseEntity<Integer>, Integer> getInputRepository(){
        return getRepository(inputType, inputPath);
    }

    @Bean(name = "output")
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public CrudRepository<BaseEntity<Integer>, Integer> getOutputRepository(){
        return getRepository(outputType, outputPath);
    }

    private CrudRepository<BaseEntity<Integer>, Integer> getRepository(String repoType, String path){
        CrudRepository<BaseEntity<Integer>, Integer> result = null;
        switch (repoType){
            case TYPE_CSV:  result = new CrudRepositoryImp<>(path, Program.getClazz()); break;
            case TYPE_JDBC:
                result = new JdbcCrudRepositoryImpl<>(Program.startServer(path), Program.getClazz());
                break;
        }
        return result;
    }
}
