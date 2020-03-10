package ua.ithillel.dnepr.roman.gizatulin.ioc;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RenoEngine implements Engine {
    @Override
    public String dirDirDir(String oil) {
        return "Reno dirDirDir " + oil;
    }
}
