package ua.ithillel.dnepr.yuriy.shaynuk.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.repository.CrudRepository;
import ua.ithillel.dnepr.common.repository.entity.BaseEntity;

import java.util.List;
import java.util.Optional;

@Component
@Configuration
@ComponentScan({
        "ua.ithillel.dnepr.yuriy.shaynuk.ioc"
})

@Slf4j
public class TransferData{
    private CrudRepository<BaseEntity<Integer>, Integer> inputRepository;
    private CrudRepository<BaseEntity<Integer>, Integer> outputRepository;

    @Autowired
    public TransferData(@Qualifier("input") CrudRepository<BaseEntity<Integer>, Integer> inputRepository, @Qualifier("output") CrudRepository<BaseEntity<Integer>, Integer> outputRepository) {
        this.inputRepository = inputRepository;
        this.outputRepository = outputRepository;
    }

    public void start() {
        //Optional<List<BaseEntity<Integer>>> inputList = inputRepository.findAll();
        Optional<BaseEntity<Integer>> inputList = inputRepository.findById(4400);
        inputList.ifPresent(entity -> outputRepository.create(entity));
        log.warn("test");
        //
    }
}
