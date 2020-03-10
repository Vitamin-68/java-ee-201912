package ua.ithillel.dnepr.roman.gizatulin.ioc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Reno implements Car, EngineInjector {
    private Engine engine;
    private List<RenoDoor> renoDoorList;

    public Reno() {
    }

    @Autowired
    public Reno(Engine engine) {
        this.engine = engine;
    }

    @Qualifier("reno")
    public List<RenoDoor> getRenoDoorList() {
        return renoDoorList;
    }

    @Autowired
    public void setRenoDoorList(List<RenoDoor> renoDoorList) {
        this.renoDoorList = renoDoorList;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public String run(String speed) {
        engine.dirDirDir("2");
        return "reno dirDirDir";
    }

    @Override
    public void addEngine(Engine engine) {
        this.engine = engine;
    }
}
