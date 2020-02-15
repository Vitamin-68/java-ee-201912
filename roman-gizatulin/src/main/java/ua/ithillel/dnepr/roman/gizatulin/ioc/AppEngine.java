package ua.ithillel.dnepr.roman.gizatulin.ioc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ComponentScan({
        "ua.ithillel.dnepr.roman.gizatulin.ioc"
})
public class AppEngine {
    private Car car;
    private Engine engine;

    public AppEngine(Car car, Engine engine) {
        this.car = car;
        this.engine = engine;
    }

    private static int doorCount = 0;

    @Bean("reno")
    public Door getDoor2() {
        Door renoDoor = new RenoDoor();
        renoDoor.setOpen(doorCount++ % 2 == 0);
        return renoDoor;
    }

    @Bean("subaru")
    public Door getDoor1() {
        Door renoDoor = new SubaruDoor();
        renoDoor.setOpen(doorCount++ % 2 == 0);
        return renoDoor;
    }

    public void start(String[] args) {
        car.run("sfgswetgaf");
    }
}
