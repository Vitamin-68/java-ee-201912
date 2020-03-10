package ua.ithillel.dnepr.roman.gizatulin.ioc;

public final class CarFactory {
    public static Car createRenoCar(Engine engine) {
        Car car = new Reno(engine);
        // setup car
        return car;
    }
}
