package ua.ithillel.dnepr.yuriy.shaynuk.ioc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ua.ithillel.dnepr.common.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@Component
@Configuration
@ComponentScan({
        "ua.ithillel.dnepr.yuriy.shaynuk.ioc"
})

@Slf4j
public class TransferData{
    private CrudRepository<?, Integer> inputRepository;
    private CrudRepository<?, Integer> outputRepository;

    @Autowired
    public TransferData(@Qualifier("input") CrudRepository<?, Integer> inputRepository, @Qualifier("output") CrudRepository<?, Integer> outputRepository) {
        this.inputRepository = inputRepository;
        this.outputRepository = outputRepository;
    }

    public void start() {
        Optional<? extends List<?>> inputList = inputRepository.findAll();
        Optional<? extends List<?>> outputList = outputRepository.findAll();
    }
}
